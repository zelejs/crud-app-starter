import React from 'react';
import { Input, Button, Tooltip, Space } from 'antd';
import { QuestionOutlined } from '@ant-design/icons';

export default ({ props, ...rest }) => {

    function contentforHtml() {
        return (
            <p>使用特殊符号来获得动态数据
                <br /><br />[Y]:年&nbsp;[M]:月&nbsp;[D]:&nbsp;日<br />[H]:时&nbsp;[I]:分&nbsp;[S]:秒&nbsp;[W]:星期&nbsp;[U]:用户名
                <br />[F]:流程名&nbsp;[N]:编号（取值并自动增加计数值）
                <br /><br />编号例子
                <br /><br />[Y]为&nbsp;2021
                <br />[YY]为&nbsp;21
                <br />[YYYY]为&nbsp;2021
                <br />[F]为&nbsp;流程单<br />[M]为&nbsp;5<br />[D]&nbsp;为&nbsp;10<br />[N]&nbsp;为&nbsp;1开始计数的数，
                <br /><br />[F]-[Y]年[M]月[D]日-[N]<br /><br />则第1条记录编号为<br />请假流程-2021年5月10日-1<br />第2条记录编号为<br />流程单-2021年5月10日-2
            </p>
        )
    }

    return (

        <Space>
            <Input {...rest} {...props} style={{ width:"240px" }} />
            <Tooltip placement="rightTop" title={contentforHtml}>
                <Button shape="circle" icon={<QuestionOutlined />} size="small" />
            </Tooltip>
        </Space>
    )
}