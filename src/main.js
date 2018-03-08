const {app, BrowserWindow} = require("electron");
var Screen = require('screen');
var size = Screen.getPrimaryDisplay().size;
let win;
function createWindow(){
win=new BrowserWindow({
  backgroundColor:'#FEFEFE',
  width:size.width,
  height:size.height,
  kiosk: true,
  show: true,
  fullscreenable:false,  
  icon:'file://${__dirname}/dist/assets/logo.png'
})
win.loadURL('file://${__dirname}/dist/index.html');
win.on('closed',()=>{
    win=null
})
}
app.on('ready',createWindow);
app.on('window-all-closed',()=>{
  if(process.platform!=='darwin'){
    app.quit();
  }
})
app.on('activate',()=>{
  if(win===null){
    createWindow()
  }
})
