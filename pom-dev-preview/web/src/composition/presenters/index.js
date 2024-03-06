import React, { useState, useEffect } from 'react';
import { VStack, Box, HStack, Button, useToast, createIcon } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot'
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import LocalPreview from '@/composition/localPreview';
import PreviewFetch from '@/composition/localPreview/indeFetch';
import categoryListLayout from '@/composition/presenters/CategoryList/layout';
import AddPresenter from '@/composition/AddPresenter';
import { useForceUpdate } from 'zero-element-boot/lib/components/hooks/lifeCycle';
import _ from 'lodash';

export default function Index(props) {

    const { id, status, isSwitch } = props
    const api = '/openapi/lc/module'
    const layoutApi = '/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules'

    const [ currentApi, setCurrentApi ] = useState('')
    const [ currentLayoutApi, setCurrentLayoutApi ] = useState('')
    // const [previewData, setPreviewData] = useState('')

    const [ previewComponentName, setPreviewComponentName ] = useState('')
    const [isAddClick, setIsAddClick] = useState(false)
    const [currentCategoryName, setCurrentCategoryName] = useState('element')
    const toast = useToast()

    useEffect(() => {
        if(currentCategoryName){
            const cApi = `${api}?componentOption=presenter&pageNum=1&pageSize=100&combinationOption=${currentCategoryName}`
            const cLayoutApi = `${layoutApi}/160`
            setCurrentApi(cApi)
            setCurrentLayoutApi(cLayoutApi)
        }
    }, [currentCategoryName])

    const onComponentItemClick = (item) => {
        // console.log('item = ', item)
        setIsAddClick(false)
        setPreviewComponentName()
        if (item.isSelected) {
        //     setPreviewData({
        //         ___presenter2: {
        //             xname: item.componentType,
        //             props: item.componentProps
        //         }
        //     })
        setPreviewComponentName(item.previewComponentName)
        }
    }

    const onCateItemClick = (item) => {
        setCurrentApi('')
        setCurrentLayoutApi('')
        setCurrentCategoryName(item.name)
        setIsAddClick(false)
        setPreviewComponentName()
    }

    //新增
    const addNewClick = () => {
        if(currentCategoryName){
            setIsAddClick(true)
            setPreviewComponentName()
        }else{
            toastTips('请选择分类')
        }
    }

    //新增回调事件
    const cb = (status) => {
        if (status === 'success') {
            const nApi = _.cloneDeepWith(currentApi)
            const nLayoutApi = _.cloneDeepWith(currentLayoutApi)
            setCurrentApi()
            setCurrentLayoutApi()
            setIsAddClick(false)
            setTimeout(() => {
                setCurrentApi(nApi)
                setCurrentLayoutApi(nLayoutApi)
            },100)
            useForceUpdate()
        }else if (status === 'error'){
            setIsAddClick(false)
          }
    }

    // tips
    function toastTips(text, status = 'success') {
        toast({
            title: text,
            description: "",
            status: status,
            duration: 3000,
            isClosable: true,
            position: 'top'
        })
    }

    return (
        <VStack align='stretch' spacing='0'>
            <HStack spacing={'0'}>
                <Box style={{ width: '200px', height: '100vh', padding: '0 20px', background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <AutoLayout layout={categoryListLayout} onItemClick={onCateItemClick} />
                </Box>
                <Box style={{ width: '6px', height: '100vh' }} background={'#EDECF1'}></Box>
                <Box style={{ width: '600px', height: '100vh', padding: '0px 8px 0px 8px', background: '#fff' }}>
                    { isSwitch && (
                        <Button style={{marginTop: '8px'}} onClick={addNewClick}>新增</Button>
                    )}
                    {
                        currentApi && currentLayoutApi && <PreviewAutoLayout layoutApi={currentLayoutApi} api={currentApi} onItemClick={onComponentItemClick} onAddNewClick={addNewClick} isSwitch={false} isScroll={false} />
                    }
                </Box>
                <Box style={{ width: '100%', height: '100vh' }} background={'#EDECF1'}>
                    {
                        isAddClick ? (
                            <Box style={{ height: '100vh', padding: '8px', marginLeft: '6px', background: '#fff' }}>
                                <AddPresenter cb={cb} combinationOption={currentCategoryName} />
                            </Box>
                        ):(
                            previewComponentName ? (
                            <Box style={{ width: '100%', height: '100vh', padding: '8px' }} background={'#EDECF1'}>
                                {/* <LocalPreview previewData={previewData} type='presenter' /> */}
                                <PreviewFetch previewComponentName={previewComponentName}/>
                            </Box>
                            ) : <></>
                        )
                        
                    }
                </Box>
            </HStack>
        </VStack>
    )

}