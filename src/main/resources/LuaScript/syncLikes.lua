local keys = redis.call('KEYS', 'post:likes:*')
local results = {}

for i, key in ipairs(keys) do
    local value = redis.call('GET', key)
    if value then
        local postId = string.match(key, 'post:likes:(%d+)')
        table.insert(results, {postId, value})
        redis.call('DEL', key)
    end
end

return results