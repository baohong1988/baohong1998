const uuid = require('uuid/v4')

const createUser = ({name = ""} = {} ) =>( 
    {
    id: uuid(),
    name
    }
)

module.exports ={createUser}