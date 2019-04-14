import React, { Component } from 'react';
import { Button, Container, Row, Col } from 'react-bootstrap';
import App from './App'
import './Biglobby.css'
import {GET_LIST, LOGOUT} from './Event'

class Blob extends Component
{
    constructor()
    {
        super()
        this.state = 
            { 
              back : false,
              user: null,
              userList : [],
              startLabel: "",
              isReady : false,
            
            }
       
    }
    componentDidMount(){
        this.setReady()
        this.setState({user : this.props.user})
    }
    setList = ({userList})=>{
        this.setState({ userList })
    }
    leave = () =>{
        const {socket, user,room} = this.props
        socket.emit(LOGOUT, user, room)
        this.setState({ back : true})
    }
    setStartLabel = (label)=>{
        this.setState({startLabel : label})
    }
    setReady = ()=>{
        const {isHost} = this.props
        const {isReady} = this.state
        let {user} = this.state

        if(isHost)
            this.setStartLabel("Start")
        else if(isReady)
        {
            this.setStartLabel("Unready")
            this.setState({isReady: false})
            user.readyColor = "#6cff47"
            console.log(user.readyColor)
            
        }
        else
        {
            this.setStartLabel("Ready")
            this.setState({isReady: true})
        }
    }
    render()
    {
        

        if(this.state.back) return(<App />)
        const {socket, isHost} = this.props
       
        const room = this.props.room
        
       
       
        socket.emit(GET_LIST, room,this.setList)

        let userList = []
        userList = Object.keys(this.state.userList)
      
        var i = 0;
        return(
        
            
        <div className="lobby">
            
            <p>New Lobby</p>
            <Container >
            <Row>
                <Col></Col>
                <Col ><Button variant='primary' size='lg' onClick={this.leave}>Leave</Button></Col>
                <Col ><Button variant='primary' size='lg' onClick={this.setReady}>{this.state.startLabel}</Button></Col>
                <Col></Col>           
            </Row>
            
            </Container>
            <div >
                <Container className='players'>
                    <Row  >
                        {userList.map(user => <Col  key={i++} md={4}>{user}</Col>)}
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
