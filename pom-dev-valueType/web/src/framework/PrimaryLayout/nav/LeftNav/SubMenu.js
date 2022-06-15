import React,{useState} from 'react';
import {history} from 'umi'
import { ArrowBottomSvg, ArrowTopSvg } from './svg';
export default function SubMenu({ selected, title,path, children }) {
  const classes = [
    'nav-item-container',
    selected ? 'selected' : '',
  ].join(' ');
  const [show,setShow] = useState(selected)
  const handleShow=()=>{
    if(selected){
      setShow(!show)
    }else{
      setShow(true)
    }
    history.push(path)
  }

  
  // console.log(show,"SHOW",selected,"SELECTED")

  return <div className={classes}>
    <div className="nav-titleBox" onClick={handleShow}><span>{title}</span>
      <span style={{position:"absolute",top:"12px",right:"5px"}}>{selected&&show?<ArrowBottomSvg/>:<ArrowTopSvg/>}</span>
    </div>
    
    <div style={{display:selected&&show?"block":"none"}}>{children}</div>
  </div>
}