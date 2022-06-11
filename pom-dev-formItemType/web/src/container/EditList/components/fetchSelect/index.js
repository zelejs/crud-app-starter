/* eslint-disable no-restricted-globals */
import React, { useState, useEffect } from 'react';
import { message, Select, Spin } from 'antd';
import { get as getEndpoint } from 'zero-element/lib/utils/request/endpoint';
import { useDidMount, useForceUpdate } from 'zero-element/lib/utils/hooks/lifeCycle';
import promiseAjax from '@/utils/promiseAjax';
import _ from 'lodash';
import './index.less'

const { Option } = Select;

/**
 * 
 * @param { 获取数据源字段 } dataField 
 */

export default function FetchSelect(props) {

    const { formData, options, onChange, field, cb } = props

    const { 
        API, 
        dataField = 'records',
        label: optLabel = 'label',
        value: optValue = 'value',
        query,
    } = options;


    const [defaultValue, setDefaultValue] = useState('')
    const [loading, setLoading] = useState(undefined);
    const [listData, setListData] = useState('');

    
    const searchList = location.search.split('=');
    const id = searchList[1];

    useEffect(_ => {
        if(id && API){
            getData(id)
        }else{
            console.error('获取id失败')
        }
    }, [id])

    useEffect(_ => {
        
        if(formData){
            setDefaultValue(formData[field])
        }
    }, [formData])

    const getData = (id) =>  {
        const url = API.replace('(id)', id)
        setLoading(true)
        promiseAjax(`${getEndpoint()}${url}`)
            .then(resp => {
                if (resp && resp.code === 200) {
                    const respData = formatListData(_.get(resp, dataField, []))
                    setListData(respData)
                } else {
                    message.error('修改失败')
                }
            })
            .finally(_ => {
                setLoading(false)
            })
    }

    //处理成 Select组件适配的数据
    function formatListData (list) {
        const formatData =[]
        if(list && JSON.stringify(list) != '[]'){
            list.map(item => {
                const formatItem = {}
                formatItem[optLabel] = item[optValue]
                formatData.push(formatItem)
            })
        }
        return formatData
    }

    //回调数据
    function handleChange(data) {
        setDefaultValue(data)
        if(cb){
            const value = {}
            value[field] = data
            cb(value)
        }
    }

    return <Spin spinning={loading}>
        <Select placeholder='请选择' style={{ width: '100%' }} value={defaultValue} onChange={handleChange}>
            
            {
                listData && listData.map((item, index) => (
                    <Option key={`${item[optLabel]}_selectitem`} value={item[optLabel]}>
                        {item[optLabel]}
                    </Option>
                ))
            }
        </Select>
    </Spin>
}
