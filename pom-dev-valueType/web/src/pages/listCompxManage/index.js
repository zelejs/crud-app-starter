import React from 'react';

import { Row, Col } from 'antd';

import listData from './listJsonData';

require('./index.less')

export default function (){

    function listItem (item, index){
        const CompxValue = item.compx;
        return (
            <Row align='left' justify='left' key={`${index}_item`} style={{marginBottom:'8px'}}>
                
                <Col flex="100px" align='center' justify='center'style={{ height: '35px'}}>
                    <div className='padding-t-b-10' style={{width: '100%', height: '100%'}}>{item.label}</div>
                </Col>
                <Col flex="200px" align='center' justify='center'>
                    <div className='padding-t-b-10' style={{width: '100%', height: '100%'}}>{item.describe}</div>
                </Col>
                <Col flex="120px" align='center' justify='center'>
                    <div className='padding-t-b-10' style={{width: '100%', paddingLeft: '5px' }}>{ CompxValue ? <CompxValue {...item.value} />: null }</div>
                </Col>
                {/* <Col flex="150px" align='center' justify='center'>
                    <div className='padding-t-b-10 padding-l-r-20' style={{width: '100%', height: '100%'}}>{item.type}</div>
                </Col> */}
                {/* <Col flex="300px">
                    <div className='padding-t-b-10' style={{width: '100%', height: '100%',paddingLeft: '5px',}}>
                        <textarea rows="8" cols="40" style={{resize:'none'}} disabled >{item.jsonFormat}</textarea>
                    </div>
                </Col> */}
            </Row>
        )
    }

    return (
        <div className='list-body'>
            <Row align='left' justify='left' style={{marginBottom: '10px'}}>
                <Col flex="100px" align='center'style={{ height: '35px'}}>组件名</Col>
                <Col flex="200px" align='center'>描述</Col>
                <Col flex="120px" align='center'>组件效果</Col>
                {/* <Col flex="150px" align='center'>支持参数类型</Col> */}
                {/* <Col flex="300px" align='center'>代码块</Col> */}
            </Row>
            {listData.records.map((item, index) => {
                return listItem(item ,index)
            })}
        </div>
    )
};
