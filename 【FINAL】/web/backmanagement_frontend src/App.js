import React, { Component } from 'react';
import HomePage from './HomePage/HomePage';
import Management from './Management/Management';
import Search from './Search/Search';
import Statistics from './Statistics/Statistics';
import Login from './User/Login';
import Register from './User/Register'
import AddUser from './Management/AddUser';
import DeleteUser from './Management/DeleteUser';
import ModifyUser from './Management/ModifyUser';
import AddShift from './Management/AddShift';
import DeleteShift from './Management/DeleteShift';
import ModifyShift from './Management/ModifyShift';
import AddDriver from './Management/AddDriver';
import DeleteDriver from './Management/DeleteDriver';
import ModifyDriver from './Management/ModifyDriver';
import SearchUser from './Search/SearchUser';
import SearchMap from './Search/SearchMap';
import SearchInShift from './Search/SearchInShift';
import SearchOutShift from './Search/SearchOutShift';
import SearchReserved from './Search/SearchReserved';
import AddMessage from './Management/AddMessage';
import UserStatistics from './Statistics/UserStatistics';
import AppointmentStatistics from './Statistics/AppointmentStatistics';

import './App.css';
import { HashRouter as Router, Route } from "react-router-dom";
import {Switch} from "react-router";


class App extends Component {
  render() {
    return (
        <Router>
            <div>
                <Switch>
                    <Route exact path="/" component={HomePage}/>
                    <Route exact path="/management" component={Management}/>
                    <Route exact path="/search" component={Search}/>
                    <Route exact path="/statistics" component={Statistics}/>
                    <Route exact path="/login" component={Login}/>
                    <Route exact path="/register" component={Register}/>
                    <Route exact path="/adduser" component={AddUser}/>
                    <Route exact path="/deleteuser" component={DeleteUser}/>
                    <Route exact path="/modifyuser" component={ModifyUser}/>
                    <Route exact path="/addshift" component={AddShift}/>
                    <Route exact path="/deleteshift" component={DeleteShift}/>
                    <Route exact path="/adddriver" component={AddDriver}/>
                    <Route exact path="/deletedriver" component={DeleteDriver}/>
                    <Route exact path="/modifydriver" component={ModifyDriver}/>
                    <Route exact path="/modifyshift" component={ModifyShift}/>
                    <Route exact path="/searchuser" component={SearchUser}/>
                    <Route exact path="/searchmap" component={SearchMap}/>
                    <Route exact path="/searchinshift" component={SearchInShift}/>
                    <Route exact path="/searchoutshift" component={SearchOutShift}/>
                    <Route exact path="/searchreserved" component={SearchReserved}/>
                    <Route exact path="/addmessage" component={AddMessage}/>
                    <Route exact path="/userstatistics" component={UserStatistics}/>
                    <Route exact path="/appointmentstatistics" component={AppointmentStatistics}/>
                </Switch>
            </div>
        </Router>

    );
  }
}

export default App;
