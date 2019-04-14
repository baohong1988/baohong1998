import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { Form, Button, Alert } from 'react-bootstrap';
import { VERIFY_USER, VERIFY_ROOM} from '../Event'
class StartModal extends Component
{
    constructor()
    {
        super()

        this.state = {
            roomname: "",
            username: "",
            hostname:"",
            error: "",
            invalid: false,
            roomTaken: false,
            userTaken: false,
            roomExist: false
            
        }
    }
    setUser = ({user, room, isUser, isRoom})=>{
        const {roomname} = this.state
        console.log(roomname)
        if(!isRoom)
        {    
            this.setError("Room doesn't exist")
            this.setState({roomExist:true})
            return
        }
        if(this.state.username === "")
        {    
            this.setError("Please enter a name")
            this.setState({userTaken: true})
            return
        }
        if(isUser && isRoom){
            this.setError("User name taken")
            this.setState({userTaken:true})
            
        
        }else{
            this.props.setUser(user, room)
            this.setState({error : ""})
            this.props.setcreated()
        }
        
    }
    setRoom = ({room, host, isRoom})=>{
       
        // console.log(hostname)
        if(isRoom){
            this.setError("Room name taken")
            this.setState({roomTaken:true})
        
        }
        else if(room === "")
        {
            this.setError("Please enter a name")
            this.setState({roomTaken: true})
        }    
        else{
            this.props.setRoom(room, host)
            this.setState({error : ""})
            this.props.setcreated()
        }
        
    }
    setError = (error)=>{
        this.setState({error})
    }
    handleOnSubmit = () =>
    {
        //console.log(this.state.username)
        const { socket } = this.props
        let { hostname } = this.state
        hostname = hostname.trim()

        socket.emit(VERIFY_ROOM, hostname, this.setRoom)
            
          
        // this.isProceed(this.state.error)
        // console.log(this.state.error)
    }
    handleOnSubmit2 = ()=>
    {
        const { socket } = this.props
        let { username } = this.state
        let { roomname } = this.state
       
        username = username.trim()
        roomname = roomname.trim()

        socket.emit(VERIFY_USER, username, roomname, this.setUser)
    }
   
    // isProceed = (error) =>{
    //     if(error == "")
    //         this.props.setcreated()
        
    // }
    handleOnChange1 = (e) =>{
       
        this.setState({hostname:e.target.value})
        this.setRoomTaken()
        
    }
    handleOnChange2 = (e) =>{
        this.setState({username:e.target.value})
        this.setUserTaken()
    }
    handleOnChange3 = (e) =>{
        this.setState({roomname:e.target.value})
        this.setRoomExist()
    }
   
    setRoomTaken=()=>{
        this.setState({roomTaken:false})
    }
    setUserTaken=()=>{
        this.setState({userTaken:false})
    }
    setRoomExist=()=>{
        this.setState({roomExist:false, userTaken:false})
    }
    onHide = ()=>{
        this.setState({roomExist:false, userTaken:false, roomTaken: false})
        this.props.onHide()
    }
    render()
    {
        let { username,error,roomname,hostname,roomTaken,userTaken,roomExist } = this.state
        //console.log(this.textInput)
        switch(this.props.ishost)
        {
            
            case 'true':
            return(
                <Modal show={this.props.show} onHide={this.props.onHide} size="lg"  >
                    <Modal.Header closeButton>
                        <Modal.Title>
                            Create a lobby
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Form.Group controlId="createRoom">
                                <Form.Label>Room name</Form.Label>
                                <Form.Control
                                    ref={(input) => {this.textInput = input}} 
                                    type="text"
                                    value={hostname}
                                    onChange={this.handleOnChange1}
                                    placeholder="Create room name" />
                            </Form.Group>
                            <Alert variant='danger' show={roomTaken} onClose={this.setRoomTaken}>{error}</Alert>
                            <Form.Group controlId="creatNumPlayers">
                                <Form.Label>Number of players</Form.Label>
                                <Form.Control 
                                    type="number" 
                
                                    placeholder="Enter number of players (minimum is 4)" />
                            </Form.Group>

                            <Form.Group controlId="createPass">
                                <Form.Label>Password (optional)</Form.Label>
                                <Form.Control 
                                    type="password" 
                                    placeholder="Create password" />
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.onHide}>
                            Close
                        </Button>
                        <Button variant="primary" onClick={this.handleOnSubmit}>
                            Create game
                        </Button>
                    </Modal.Footer>
        

                </Modal>
            );
            default:
            return(
                <Modal show={this.props.show} onHide={this.props.onHide} size="lg" >
                    <Modal.Header closeButton>
                        <Modal.Title>
                            Join a lobby
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Form.Group controlId="createUser">
                                <Form.Label>Create your player name</Form.Label>
                                <Form.Control 
                                    ref={(input) => {this.textInput = input}} 
                                    type="text"
                                    value={username} 
                                    onChange={this.handleOnChange2} 
                                    placeholder="Enter a name" />
                            </Form.Group>
                            <Alert variant='danger' show={userTaken} onClose={this.setUserTaken}>{error}</Alert>
                            <Form.Group controlId="findRoom">
                                <Form.Label>Host name</Form.Label>
                                <Form.Control
                                    ref={(input) => {this.textInput = input}} 
                                    type="text"
                                    value={roomname} 
                                    onChange={this.handleOnChange3} 
                                    placeholder="Enter host name" />
                            </Form.Group>
                            <Alert variant='danger' show={roomExist} onClose={this.setRoomExist} >{error}</Alert>
                            <Form.Group controlId="createPass">
                                <Form.Label>Password</Form.Label>
                                <Form.Control 
                                    type="password" 
                                    placeholder="Enter host password" />
                            </Form.Group>
                        </Form>
                    </Modal.Body>
    
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.onHide}>
                            Close
                        </Button>
                        <Button variant="primary" onClick={this.handleOnSubmit2}>
                            Join Game
                        </Button>
                    </Modal.Footer>
        
    
                </Modal>
            );

        }
    }
}


export default StartModal