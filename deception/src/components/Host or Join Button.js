import React, { Component } from 'react';
import { Button } from 'react-bootstrap';


class HJButton extends Component
{
    render()
    {
        
        return (
            <div>
                <Button variant="primary" size="lg" onClick={this.props.sethost}>
                {this.props.type}
                </Button>
    
            </div>
            
        );
        
    }
}

export default HJButton;