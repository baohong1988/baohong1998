const uuid = require('uuid/v4')

const createUser = ({name = ""} = {} ) =>( 
    {
    id: uuid(),
    name,
    readyColor: "white"
    
    }
)

module.exports ={createUser}