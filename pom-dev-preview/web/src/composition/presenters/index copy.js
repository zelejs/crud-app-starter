import React, { useState, useEffect } from 'react';
import { VStack, Box, HStack, Button } from '@chakra-ui/react';
import { history } from 'umi';
import { AutoLayout } from 'zero-element-boot'
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview';
import categoryListLayout from '@/composition/presenters/CategoryList/layout';
import AddPresenter from '@/composition/AddPresenter';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

const routeMap = {
    presenter: '/presenters',
    cart: '/carts',
    layout: '/layouts',
    container: '/containers',
    finish: '/view'
}

const apiParamsMap = {
    presenter: 'presenter',
    cart: 'cart',
    layout: 'layout',
    container: 'container',
}

const apiIdMap = {
    presenter: '160',
    cart: '',
    layout: '',
    container: '',
}

export default function Index(props) {

    const { id, status, isSwitch } = props
    const api = '/openapi/lc/module'
    const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules'

    const [ currentApi, setCurrentApi ] = useState('')
    const [ currentLayoutApi, setCurrentLayoutApi ] = useState('')
    const [previewData, setPreviewData] = useState('')
    const [isAddClick, setIsAddClick] = useState(false)
    const [currentAddType, setCurrentAddType] = useState('element')

    useEffect(() => {
      const cApi = `${api}?componentOption=presenter&moduleType=${currentAddType}`
      const cLayoutApi = `${layoutApi}/160`
      setCurrentApi(cApi)
      setCurrentLayoutApi(cLayoutApi)
    }, [currentAddType])


    //保存数据
    function saveData(itemData) {
        let api = '/openapi/lc/module/build-auto-layout/' + id
        const queryData = {
            addModuleId: itemData.id
        };
        promiseAjax(api, queryData, { method: 'PATCH' }).then(resp => {
            if (resp && resp.code === 200) {
                toPage(resp.data.nextComponent)
            } else {
                console.error("添加presenter失败 = ", resp)
                toastTips(resp.message)
            }
        }).finally(_ => {
            // setLoading(false)
        });
    }

    //更换
    function editData(itemData) {
        let api = '/openapi/lc/module/AutoLayout/replaceModule/' + id
        const queryData = {
            replaceModuleId: itemData.id
        };
        promiseAjax(api, queryData, { method: 'PUT' }).then(resp => {
            if (resp && resp.code === 200) {
                goViewPage()
            } else {
                console.error("更换presenter失败 = ", resp)
                toastTips(resp.message)
            }
        }).finally(_ => {
            // setLoading(false)
        });
    }

    const toPage = (nextComponent) => {
        const path = routeMap[nextComponent]
        history.push({
            pathname: path,
            query: {
                id
            }
        })
    }

    //返回详情页
    function goViewPage() {
        history.push({
            pathname: '/view',
            query: {
                id
            }
        })
    }

    const onComponentItemClick = (item) => {
        // console.log('item = ', item)
        setIsAddClick(false)
        setPreviewData()
        if (item.isSelected) {
            setPreviewData({
                ___presenter2: {
                    xname: item.componentType,
                    props: item.componentProps
                }
            })
        }
    }

    const onCateItemClick = (item) => {
        console.log(' item = ', item)
        setCurrentApi('')
        setCurrentLayoutApi('')
        setCurrentAddType(item.name)
        setIsAddClick(false)
        setPreviewData()
    }

    //新增
    const addNewClick = () => {
        console.log(' presenter add click')
        setIsAddClick(true)
        setPreviewData()
    }

    //新增回调事件
    const cb = (status) => {
        if (status === 'success') {
            console.log('新增成功')
            setCurrentApi()
            setCurrentLayoutApi()
            setIsAddClick(false)
            setTimeout(() => {
                setCurrentApi(api)
                setCurrentLayoutApi(layoutApi)
            },100)
        }
    }

    return (
        <VStack align='stretch' spacing='-2'>
            <HStack spacing={'0'}>
                <Box style={{ width: '200px', height: '100vh', padding: '0 20px', background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <AutoLayout layout={categoryListLayout} onItemClick={onCateItemClick} />
                </Box>
                <Box style={{ width: '6px', height: '100vh' }} background={'#EDECF1'}></Box>
                <Box style={{ height: '100vh', padding: '8px', background: '#fff' }}>
                    <Button onClick={addNewClick}>新增</Button>
                    {
                        currentApi && currentLayoutApi && <PreviewAutoLayout layoutApi={currentLayoutApi} api={currentApi} onItemClick={onComponentItemClick} onAddNewClick={addNewClick} isSwitch={isSwitch} />
                    }
                </Box>
                <Box style={{ width: '100%', height: '100vh' }} background={'#EDECF1'}>
                    {
                        isAddClick ? (
                            <Box style={{ height: '100vh', padding: '8px', marginLeft: '6px', background: '#fff' }}>
                                <AddPresenter cb={cb} />
                            </Box>
                        ):(
                            previewData ? (
                            <Box style={{ width: '100%', height: '100vh', padding: '8px' }} background={'#EDECF1'}>
                                <LocalPreview previewData={previewData} type='presenter' />
                            </Box>
                            ) : <></>
                        )
                        
                    }
                </Box>
            </HStack>
        </VStack>
    )

}