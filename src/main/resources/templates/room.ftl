<!doctype html>
<html lang="en">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        [v-cloak] {
            display: none;
        }
        .chat-container {
            max-width: 800px;
            margin: 50px auto 0;
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .chat-header {
            text-align: center;
            margin-bottom: 20px;
        }
        .room-list {
            max-height: 300px;
            overflow-y: auto;
            margin-bottom: 20px;
        }
        .input-group {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container chat-container" id="app" v-cloak>
    <div class="row">
        <div class="col-md-12">
            <div class="chat-header">
                <h3>채팅방 리스트</h3>
            </div>
        </div>
    </div>
    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">방제목</label>
        </div>
        <input type="text" class="form-control" v-model="room_name" v-on:keyup.enter="createRoom">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="createRoom">채팅방 개설</button>
        </div>
    </div>
    <ul class="list-group room-list">
        <li class="list-group-item list-group-item-action" v-for="item in chatrooms" v-bind:key="item.roomId" v-on:click="enterRoom(item.roomId)">
            {{ item.name }}
        </li>
    </ul>
</div>
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script>
    const vm = new Vue({
        el: '#app',
        data: {
            room_name: '',
            chatrooms: []
        },
        created() {
            this.findAllRoom();
        },
        methods: {
            findAllRoom: function() {
                axios.get('/chat/rooms').then(response => { this.chatrooms = response.data; });
            },
            createRoom: function() {
                if (this.room_name === "") {
                    alert("방 제목을 입력해 주십시요.");
                } else {
                    const params = new URLSearchParams();
                    params.append("name", this.room_name);
                    axios.post('/chat/room', params)
                        .then(response => {
                            alert(response.data.name + " 방 개설에 성공하였습니다.");
                            this.room_name = '';
                            this.findAllRoom();
                        })
                        .catch(response => {
                            alert("채팅방 개설에 실패하였습니다.");
                        });
                }
            },
            enterRoom: function(roomId) {
                const sender = prompt('유저 이름을 입력해 주세요.');
                if (sender !== "") {
                    // AJAX 요청을 통해 유저를 생성
                    fetch('/member/create', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ name: sender, roomId: roomId })
                    })
                        .then(response => {
                            if (response.ok) {
                                return response.json();
                            } else {
                                throw new Error('Failed to create user');
                            }
                        })
                        .then(data => {
                            // 유저 생성 성공 시 로컬 스토리지에 저장하고 채팅방으로 이동
                            localStorage.setItem('chat.sender', sender);
                            localStorage.setItem('chat.roomId', roomId);
                            location.href = "/chat/room/enter/" + roomId;
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert('유저 생성에 실패했습니다. 다시 시도해 주세요.');
                        });
                }
            }
        }
    });
</script>
</body>
</html>
