import React, { Component } from 'react';
import { Button, Container, Row, Col } from 'react-bootstrap';
import App from './App'
import './Biglobby.css'
import {GET_LIST} from './Event'

class Blob extends Component
{
    constructor()
    {
        super()
        this.state = 
            { 
              back : false,
              userList : []
            }
    }
    setList = ({userList})=>{
        this.setState({ userList })
    }
   
    render()
    {
        

        if(this.state.back) return(<App />)
        const {socket} = this.props
        let goBack = () => {
            this.setState({ back : true})
            
        }
        
        const room = this.props.room
        console.log("room :" + room)
        socket.emit(GET_LIST, room,this.setList)
        let userList = []
 
        userList = Object.keys(this.state.userList)
        //console.log(this.state.userList)
        // var i = 0
        // for(user in this.state.userList)
        // {
        //     i++;
        //     players1.push(<Col key={i} md={4}>{user}</Col>)
        // }
        var i = 0;
        return(
        
            
        <div className="lobby">
            
            <p>New Lobby</p>
            <Container >
            <Row>
                <Col ><Button variant='primary' size='lg' onClick={goBack}>Leave</Button></Col>

            </Row>
            
            </Container>
            <div >
                <Container className='players'>
                    <Row>
                        {userList.map(user => <Col key={i++} md={4}>{user}</Col>)}
                    </Row>
                </Container>
            </div>
        </div>
        )
    }
}



export default Blob
// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
//serviceWorker.unregister();
