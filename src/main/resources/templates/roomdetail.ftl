<!doctype html>
<html lang="en">
<head>
    <title>Websocket ChatRoom</title>
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
        .message-list {
            max-height: 300px;
            overflow-y: auto;
            margin-bottom: 20px;
        }
        .message-item {
            margin-bottom: 10px;
        }
        .user-list {
            max-height: 200px;
            overflow-y: auto;
        }
        .input-group {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container chat-container" id="app" v-cloak>
    <div class="chat-header">
        <h2>{{ room.name }}</h2>
    </div>
    <div class="user-list">
        <h4>Users:</h4>
        <ul class="list-group">
            <li class="list-group-item" v-for="user in users" :key="user.id">
                {{ user.name }}
            </li>
        </ul>
    </div>
    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">내용</label>
        </div>
        <input type="text" class="form-control" v-model="message" v-on:keypress.enter="sendMessage">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="sendMessage">보내기</button>
        </div>
    </div>
    <ul class="list-group message-list">
        <li class="list-group-item message-item" v-for="msg in messages" :key="msg.timestamp">
            <strong>{{ msg.sender }}</strong>: {{ msg.message }}
        </li>
    </ul>
</div>
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    let ws = new WebSocket("ws://localhost:8081/ws-stomp");
    let stompClient = Stomp.over(ws);
    let reconnect = 0;

    const vm = new Vue({
        el: '#app',
        data: {
            roomId: '',
            room: {},
            users: [],
            sender: '',
            message: '',
            messages: [],
        },
        created() {
            this.roomId = localStorage.getItem('chat.roomId');
            this.sender = localStorage.getItem('chat.sender');
            this.findRoom();
            this.loadMessages();
            this.loadUsers();
            window.addEventListener('beforeunload', this.leaveRoom); // 창 닫기 또는 새로고침 시 LEAVE 메시지 전송
        },
        methods: {
            findRoom: function () {
                axios.get('/chat/room/' + this.roomId).then(response => {
                    this.room = response.data;
                });
            },
            loadUsers: function () {
                axios.get('/chat/room/' + this.roomId + '/users').then(response => {
                    this.users = response.data;
                });
            },
            sendMessage: function () {
                stompClient.send("/pub/chat/message", {}, JSON.stringify({
                    type: 'TALK',
                    roomId: this.roomId,
                    sender: this.sender,
                    message: this.message,
                    timestamp: Date.now()
                }));
                this.message = '';
            },
            recvMessage: function (recv) {
                this.messages.unshift({
                    "type": recv.type,
                    "sender": recv.type === 'ENTER' ? '[알림]' : recv.sender,
                    "message": recv.message
                });
            },
            loadMessages: function () {
                axios.get('/chat/messages/' + this.roomId).then(response => {
                    this.messages = response.data;
                });
            },
            leaveRoom: function () {
                stompClient.send("/pub/chat/message", {}, JSON.stringify({
                    type: 'LEAVE',
                    roomId: this.roomId,
                    sender: this.sender,
                    timestamp: Date.now()
                }));
            }
        }
    });

    function connect() {
        stompClient.connect({}, function(frame) {
            stompClient.subscribe("/sub/chat/room/" + vm.$data.roomId, function(message) {
                const recv = JSON.parse(message.body);
                vm.recvMessage(recv);
            });
            stompClient.send("/pub/chat/message", {}, JSON.stringify({
                type:'ENTER',
                roomId:vm.$data.roomId,
                sender:vm.$data.sender,
                timestamp: Date.now()
            }));
        }, function(error) {
            if (reconnect++ <= 5) {
                setTimeout(function() {
                    console.log("connection reconnect");
                    ws = new WebSocket("ws://localhost:8081/ws-stomp");
                    stompClient = Stomp.over(ws);
                    connect();
                }, 10 * 1000);
            }
        });
    }
    connect();
</script>
</body>
</html>
