import React, { Component } from 'react';
import HJButton from './components/Host or Join Button';
import StartModal from './components/Start game Modal'
import { Container, Row, Col, Modal } from 'react-bootstrap';
import { BrowserRouter, Route, Switch } from 'react-router-dom'
import Blob from './Big lobby';
import './App.css';
import io from 'socket.io-client'
import { USER_CONNECTED, LOGOUT, CREATE_ROOM } from './Event'
const socketURL = "http://192.168.0.10:3231"

class App extends Component {
  constructor()
    {
        super();
        this.state = {
            isHost : false,
            isShow: false,
            isCreated: false,
            socket : null,
            user : null,
            room : null
        }
        
    }

  initSocket = ()=>{
    const socket = io(socketURL)
    socket.on('connect', ()=>{
        console.log("Connected")
    })
    this.setState({socket})
  }
  setUser = (user, room)=>{
    const { socket } = this.state
    socket.emit(USER_CONNECTED, user, room);
    this.setState({ user })
    this.setState({ room})
  }
  setRoom = (room, hostname)=>{
    const { socket } = this.state
    socket.emit(CREATE_ROOM, room, hostname);
    this.setState({ room })
    
   
  }
  logout = () =>{
    let { socket } = this.state.socket
    socket.emit(LOGOUT)
    this.setState({user:null})
  }
  componentDidMount(){
    this.initSocket()
  }
  setCreated = () => {
    this.setState({ isShow : false, isCreated : true})
  }
  render() {
    //console.log(this.state.isCreated)
    const { socket } = this.state
    let closeRegModal = () => this.setState({ isShow : false })
    let setJoin = () => {
      this.setState({isShow : true});
      this.setState({isHost : false})
    }
    let setHost = () => {
      this.setState({isShow : true});
      this.setState({isHost : true});
     
    }
    //console.log(this.state.isHost)
    
    if(this.state.isCreated)
    {
      
      return (

           <Blob socket={this.state.socket} room={this.state.room}/>
         
       
      )
    }
    return (
      <div className="App">
      
          <p>Deception</p>
          <Container >
            <Row>
              <Col ></Col>
              <Col ><HJButton type="Join game" sethost={setJoin}/></Col>
              <Col ><HJButton type="Host game" sethost={setHost}/></Col>
              <Col ></Col>
          </Row>
        
          </Container>
          <StartModal ishost={this.state.isHost.toString()} show={this.state.isShow}
                      onHide={closeRegModal} setcreated={this.setCreated} socket={socket} 
                      setUser={this.setUser}
                      setRoom={this.setRoom}/>
        
        
        
      </div>
    );
  }
}

export default App;
