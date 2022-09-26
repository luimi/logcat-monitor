const webSocket = require('ws');
const path = require('path');
const express = require('express');
const http = require('http');

const app = express();
const router = express.Router();

const users = {};
const groups = [];


router.get('/', (req,res) => {
  res.sendFile(path.join(__dirname+'/public/index.html'));
});
app.use('/', router);
const server = http.createServer(app);

server.listen(process.env.PORT || 8080);
const wss = new webSocket.Server({server: server});

const events = {
  subscribe: (ws,{group})=> {
    if(!group || group==""){
      return
    }
    ws.group = group
    if(!groups.includes(group)){
      groups.push(group)
    }
    ws.send("subscribed to "+group)
  },
  send: (ws, {group,message}) => {
    Object.keys(users).forEach(id => {
      if(users[id].group === group){
        users[id].send(message);
      }
    });
  },
  getGroups: (ws, data) => {
    ws.send(JSON.stringify({event:"groups", data:{list:groups}}));
  } 
}
wss.on('connection', (ws) => {
  ws.id = Math.random().toString(36).substring(2);
  users[ws.id] = ws;
  ws.on('close', () => {
    delete users[ws.id];
  });
  ws.on('message', (message) => {
    try{
      let income = JSON.parse(message.toString());
      events[income.event](ws,income.data);
    }catch(e){
      ws.send(JSON.stringify({event:"error",error:e}));
    }
  });
});
