const io = require('./server.js').io
const { VERIFY_USER, USER_CONNECTED, LOGOUT, GET_LIST, VERIFY_ROOM, CREATE_ROOM } = require('../Event')
const { createUser } = require('../Factories')
//let connectedUser = {}
let connectedRoom = {}
module.exports = function(socket){
    console.log("ID" + socket.id)

    socket.on(VERIFY_USER, (username, roomname, callback)=>{
        if(isRoom(roomname, connectedRoom))
        {
            if(isUser(username, connectedRoom[roomname])){
                callback({isUser : true, user : null, room: roomname, isRoom:true})
            }
            else{
                callback({isUser:false, user:createUser({name:username}), room:roomname, isRoom:true})
            }
        }
        else
        {
            callback({isUser:false, user:null, room:null, isRoom:false})
        }
        
    })
    socket.on(VERIFY_ROOM, (roomname, callback)=>{
        
        if(isRoom(roomname, connectedRoom)){
            callback({isRoom : true, room : null, host : null})
        }
        else{
            callback({isRoom:false, room: roomname, host : createUser({name:roomname})})
            
        }
    })
    socket.on(CREATE_ROOM, (room, host)=>{
        //console.log(room)
        connectedRoom = addRoom(connectedRoom, room, host)
        socket.join(room)
        //console.log(connectedRoom)
       
    })
    socket.on(USER_CONNECTED, (user, room)=>{
        //console.log(user)
        connectedRoom[room] = addUser(connectedRoom[room], user)
        socket.join(room)
        socket.user = user
        io.emit(USER_CONNECTED, connectedRoom[room])
        //console.log(connectedRoom[room])
    })
    socket.on(GET_LIST, (room, callback)=>{
        callback({userList : getlist(room)})
    })
    socket.on(LOGOUT, (user, room)=>{
        socket.leave(room)
        connectedRoom[room] = removeUser(connectedRoom[room], user.name)
     
        const len = Object.keys(connectedRoom[room]).length
        console.log(len)
        if(len === 0)
        {
            console.log(connectedRoom)
            connectedRoom = removeRoom(connectedRoom, room)
            console.log(connectedRoom)
        }
    })
}

function getlist(room){
    console.log(connectedRoom)
    return connectedRoom[room]
}
function addRoom(roomList, room, host)
{
    
    let newList = Object.assign({}, roomList)
    newList[room] = {}
    newList[room] = addUser(newList[room], host)
    return newList
}
function removeRoom(roomList,room)
{
    let newList = Object.assign({}, roomList)
    delete newList[room]
    return newList
}
function addUser(userList, user)
{
    let newList = Object.assign({}, userList)
    newList[user.name] = user
    return newList
}
function removeUser(userList, username)
{
    let newList = Object.assign({}, userList)
    delete newList[username]
    return newList
}
function isUser(username, userList){
    return username in userList
}
function isRoom(roomname, roomlist){
    return roomname in roomlist
}