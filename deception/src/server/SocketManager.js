const io = require('./server.js').io
const { VERIFY_USER, USER_CONNECTED, LOGOUT, GET_LIST } = require('../Event')
const { createUser } = require('../Factories')
let connectedUser = {}

module.exports = function(socket){
    console.log("ID" + socket.id)

    socket.on(VERIFY_USER, (username, callback)=>{
        if(isUser(username, connectedUser)){
            callback({isUser : true, user : null})
        }
        else{
            callback({isUser:false, user:createUser({name:username})})
        }
    })
    socket.on(USER_CONNECTED, (user)=>{
        connectedUser = addUser(connectedUser, user)
        socket.user = user
        io.emit(USER_CONNECTED, connectedUser)
        console.log(connectedUser)
    })
    socket.on(GET_LIST, (callback)=>{
        callback({userList : getlist()})
    })
}

function getlist(){
    return connectedUser
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