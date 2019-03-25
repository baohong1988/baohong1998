import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { Form, Button, Alert } from 'react-bootstrap';
import { VERIFY_USER} from '../Event'
class StartModal extends Component
{
    constructor()
    {
        super()

        this.state = {
            username: "",
            error: ""
            
        }
    }
    setUser = ({user, isUser})=>{
        console.log(user, isUser)
        if(isUser){
            this.setError("User name taken")
            
        
        }else{
            this.props.setUser(user)
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
        const { username } = this.state
        socket.emit(VERIFY_USER, username, this.setUser)
        
        // this.isProceed(this.state.error)
        // console.log(this.state.error)
    }
    // isProceed = (error) =>{
    //     if(error == "")
    //         this.props.setcreated()
        
    // }
    handleOnChange = (e) =>{
        this.setState({username:e.target.value})
        
    }
    render()
    {
        const { username,error } = this.state
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
                            <Form.Group controlId="creatRoom">
                                <Form.Label>Room name</Form.Label>
                                <Form.Control
                                    ref={(input) => {this.textInput = input}} 
                                    type="text"
                                    value={username} 
                                    onChange={this.handleOnChange}
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
            case 'false':
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
                                    type="username" 
                                    placeholder="Enter a name" />
                            </Form.Group>
                            <Form.Group controlId="findRoom">
                                <Form.Label>Host name</Form.Label>
                                <Form.Control 
                                    type="username" 
                                    placeholder="Enter host name" />
                            </Form.Group>
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
                        <Button variant="primary" onClick={this.props.onHide}>
                            Join Game
                        </Button>
                    </Modal.Footer>
        
    
                </Modal>
            );

        }
    }
}


export default StartModal