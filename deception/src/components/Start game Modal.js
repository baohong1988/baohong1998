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
            error: ""
            
        }
    }
    setUser = ({user, room, isUser, isRoom})=>{
        const {roomname} = this.state
        console.log(roomname)
        if(!isRoom)
        {    
            this.setError("Room doesn't exist")
            return
        }
        if(isUser && isRoom){
            this.setError("User name taken")
            
        
        }else{
            this.props.setUser(user, room)
            this.setState({error : ""})
            this.props.setcreated()
        }
    }
    setRoom = ({room, isRoom})=>{
        const {hostname} = this.state
        // console.log(hostname)
        if(isRoom){
            this.setError("Room name taken")
            
        
        }else{
            this.props.setRoom(room, hostname)
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
        let { username } = this.state
        let { roomname } = this.state
        let { hostname } = this.state
            console.log(roomname)
            socket.emit(VERIFY_ROOM, hostname, this.setRoom)
            

            
        
          
        // this.isProceed(this.state.error)
        // console.log(this.state.error)
    }
    handleOnSubmit2 = ()=>
    {
        const { socket } = this.props
        let { username } = this.state
        let { roomname } = this.state
        let { hostname } = this.state
        console.log(roomname)
        socket.emit(VERIFY_USER, username, roomname, this.setUser)
    }
   
    // isProceed = (error) =>{
    //     if(error == "")
    //         this.props.setcreated()
        
    // }
    handleOnChange1 = (e) =>{
       
        this.setState({hostname:e.target.value})
        
    }
    handleOnChange2 = (e) =>{
        this.setState({roomname:e.target.value})
        
    }
    handleOnChange3 = (e) =>{
        this.setState({username:e.target.value})
        
    }
    
    render()
    {
        let { username,error,roomname,hostname } = this.state
     
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
                            <Alert variant='danger'>{error ? error : null}</Alert>
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
                        <Button variant="secondary" onClick={this.props.onHide}>
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
                                    onChange={this.handleOnChange3} 
                                    placeholder="Enter a name" />
                            </Form.Group>
                            <Alert variant='danger'>{error ? error : null}</Alert>
                            <Form.Group controlId="findRoom">
                                <Form.Label>Host name</Form.Label>
                                <Form.Control
                                    ref={(input) => {this.textInput = input}} 
                                    type="text"
                                    value={roomname} 
                                    onChange={this.handleOnChange2} 
                                    placeholder="Enter host name" />
                            </Form.Group>
                            <Alert variant='danger'>{error ? error : null}</Alert>
                            <Form.Group controlId="createPass">
                                <Form.Label>Password</Form.Label>
                                <Form.Control 
                                    type="password" 
                                    placeholder="Enter host password" />
                            </Form.Group>
                        </Form>
                    </Modal.Body>
    
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.props.onHide}>
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