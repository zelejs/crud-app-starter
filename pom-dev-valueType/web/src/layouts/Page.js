// 居中容器
// 属性值说明：
// otherStyle：容器的其他样式
// width：容器的宽度 默认覆盖整个浏览器的宽度
// height：容器的高度 默认覆盖整个浏览器的高度
// 也可通过定义 page_container来进行对容器的样式更改
import React from 'react'

export default function Page(props){
    const {
        children,
        otherStyle, //额外样式
        width="100vw",//设定宽度
        height="100vh"//设定高度
    }=props 
    let centerStyles = { //居中样式
        display:"flex",
        "align-items":"center",
        "justify-content":"center",
        "width":width,
        "height":height,
        "margin":0,
        "padding":0,
        ...otherStyle
    }

    return <div className="page_container" style={centerStyles}>
        {Array.isArray(children)?children.map((item,i)=>{
            return item
        }):children}
    </div>
}