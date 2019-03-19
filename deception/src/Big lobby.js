import React, { Component } from 'react';
import { Button, Container, Row, Col } from 'react-bootstrap';
import App from './App'
import './Biglobby.css'

class Blob extends Component
{
    constructor()
    {
        super()
        this.state = 
            { back : false,
               }
    }
    
    render()
    {
        if(this.state.back) return(<App />)
        let goBack = () => {
            this.setState({ back : true})
        }
        const players1 = []
        
        for(var i=0; i<4; i++)
        {
            players1.push(<Col key={i} md={4}>Player {i+1}</Col>)
        }
        
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
                        {players1}
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
