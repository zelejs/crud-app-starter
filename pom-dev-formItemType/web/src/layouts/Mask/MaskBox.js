// Mask 遮罩层容器
// 容器属性：
// otherStyle：容器的额外样式，只决定中间容器的样式
// show: 是否显示 默认为false
// maskOpacity：Mask透明度 默认为0.5
// theme：big 大窗体 none 无
// bigMargin 大窗体外边框
// onChange：点击取消时执行
// 改变时 默认传回当前是否显示
// 遮罩层的层级为10000，盒子层级为10001
import React,{useState,useEffect} from 'react'

import './index.css'
import { ExitSvg } from './svg';
export default function MaskBox(props){
    const {
        children,
        otherStyle,
        show=false,
        maskOpacity=.5,
        theme,
        bigMargin,
        onChange //关闭页面时触发
    }=props
    let alignStyle;
    const [ isShow,setIsShow ] = useState( show )
    let color = "#fff"
    alignStyle = {
        "align-items":"center",
        "justify-content":"center",
        "top":0,
        "left":0,
    }
    let bigStyle = {}
    if(theme==="big"){
        color = "#000"
        bigStyle = {
            "position":"absolute",
            "top":bigMargin,
            "bottom":bigMargin,
            "left":bigMargin,
            "right":bigMargin,
        }
    }
    let styles = {
        // width:width,
        // height:height,
        "border-radius":"15px",
        "padding":"15px",
        "margin":"5px",
        "background":"white",
        ...bigStyle,
        ...otherStyle
    }
    let MaskStyles = {
        opacity: maskOpacity
    }
    let containerStyles = {
        "width":"100vw",
        "height":"100vh",
        "z-index":"10001",

        "position":"fixed",
        "display":!isShow?"none":"flex",
        ...alignStyle
    }
    useEffect(_=>{
        setIsShow(show)
    },[show])
    function hideMask(){
        setIsShow(!isShow)
        // console.log(isShow)
        onChange(isShow)
    }
    return <>{isShow?<>
    <div className="MaskBox_Mask" style={MaskStyles}></div>
    <div className="MaskBox_Container" style={containerStyles}>
        <div className="MaskBox_Body" style={styles}>
            {Array.isArray(children)?children.map((item,i)=>{
                return item
            }):children}
        </div>
        <div className="MaskBox_ExitSvg" onClick={()=>hideMask()}><ExitSvg color={color}/></div>
    </div>
    </>:null}</>
}