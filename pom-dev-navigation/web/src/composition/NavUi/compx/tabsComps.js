import React, { useState, useEffect, useRef } from 'react';
import {
    Box, Spinner, FormControl, FormLabel, Tabs, TabList, TabPanels, Tab, TabPanel,
    Button, useTab, useMultiStyleConfig, Image, Stack,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalFooter,
    ModalBody,
    ModalCloseButton,
    FormErrorMessage,
    useToast,
    Radio, RadioGroup
} from "@chakra-ui/react";
import { useForm } from 'react-hook-form';

const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import tabFormConfig from './tabsformConfig';
require('./index.less')
import pluOn from '../icons/plus-on.png';
import pluOff from '../icons/plus-off.png';
import minusOn from '../icons/minus-on.png';
import minusOff from '../icons/minus-off.png';

const formItemTypeMap = require('zero-element-boot/lib/components/config/formItemTypeConfig').get();

export default function Index(props) {

    const { items = [], onSwitchTab, isSwtich, cb } = props;
    const {
        api: { createAPI, getAPI, updateAPI, deleteAPI }
    } = tabFormConfig;

    // const [navCateListData, setNavCateListData] = useState(items)
    const [isLoading, setLoading] = useState(false)
    const [currentId, setCurrentId] = useState('')
    const [currentData, setCurrentData] = useState({})
    const [tabIndex, setTabIndex] = useState(0)
    const [isOpenEditTabModel, setIsOpenEditModel] = useState(false)
    const [isDelOpen, setIsDelOpen] = useState(false)
    const [modelTitle, setModelTitle] = useState('Title');
    const [formData, setFormData] = useState({})

    const [delValue, setDelValue] = useState('')
    const initialRef = useRef()
    const finalRef = useRef()
    const toast = useToast()

    const {
        handleSubmit,
        register,
        reset,
        formState: { errors, isSubmitting },
    } = useForm()

    // useEffect(() => {
    //     console.log('加载')
    //     setTabIndex(0)
    // }, [items]);

    //tab切换
    const switchTab = (item, index) => {
        if (index != tabIndex) {
            setTabIndex(index)
            const queryData = {
                typeId: item.id
            }
            // fetchData(navListApi, queryData)
        }
    }

    //添加tab按钮
    const CustomAddTab = React.forwardRef((props, ref) => {
        // 1. Reuse the `useTab` hook
        const tabProps = useTab({ ...props, ref })
        const isSelected = !!tabProps['aria-selected']

        // 2. Hook into the Tabs `size`, `variant`, props
        const styles = useMultiStyleConfig('Tabs', tabProps)

        return (
            <Button __css={styles.tab} {...tabProps}>
                <Box as='span' mr='0' display='flex' alignItems='center'>
                    {isSelected ? <Image src={pluOn} /> : <Image src={pluOff} />}
                </Box>
                {/* {tabProps.children} */}
            </Button>
        )
    })

    //添加tab按钮
    const CustomDelTab = React.forwardRef((props, ref) => {
        // 1. Reuse the `useTab` hook
        const tabProps = useTab({ ...props, ref })
        const isSelected = !!tabProps['aria-selected']

        // 2. Hook into the Tabs `size`, `variant`, props
        const styles = useMultiStyleConfig('Tabs', tabProps)

        return (
            <Button __css={styles.tab} {...tabProps}>
                <Box as='span' mr='0' display='flex' alignItems='center'>
                    {isSelected ? <Image src={minusOn} /> : <Image src={minusOff} />}
                </Box>
                {/* {tabProps.children} */}
            </Button>
        )
    })


    //获取详情数据
    function getData(id) {
        const api = `${getAPI.replace('(id)', id)}`;
        const queryData = {};
        setLoading(true)
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                setCurrentData(resp.data)
            } else {
                console.error("获取数据失败")
            }
        }).finally(_ => {
            setLoading(false)
        });
    }

    //新增数据
    function postData(values) {

        // let rtValue;
        // let formatApi = `${createAPI}`;
        // if(createAPI.indexOf('(') != -1){
        //   rtValue = handleChangeApiParam(createAPI)
        //   formatApi = createAPI.replace(`(${rtValue})`, currentTabItem[rtValue]);
        // }
        // const api = `${formatApi}`;
        const api = `${createAPI}`;
        const queryData = { ...values, ...formData };
        promiseAjax(api, queryData, { method: 'POST' }).then(resp => {
            if (resp && resp.code === 200) {
                toastTips('新增成功')
                cb(true)
            } else {
                console.error("新增失败 === ", resp)
                toastTips('新增失败', 'error')
            }
        }).finally(_ => {
            setIsOpenEditModel(false)
        });
    }

    //修改数据
    function putData(values, id) {

        // let rtValue;
        // let formatApi = `${updateAPI}`;
        // if(updateAPI.indexOf('(') != -1){
        //   rtValue = handleChangeApiParam(updateAPI)
        //   formatApi = updateAPI.replace(`(${rtValue})`, currentTabItem[rtValue]);
        // }
        // const api = `${formatApi}`;
        const api = `${updateAPI.replace('(id)', id)}`;
        const queryData = { ...values, ...formData };
        promiseAjax(api, queryData, { method: 'PUT' }).then(resp => {
            if (resp && resp.code === 200) {
                toastTips('修改成功')
                cb(true)
            } else {
                console.error("修改失败 == ", resp)
                toastTips('修改失败', 'error')
            }
        }).finally(_ => {
            setIsOpenEditModel(false)
        });
    }

    //删除确认提示
    function showDelModel(item) {
        if (deleteAPI && item && item.id) {
            setCurrentId(item.id)
            setIsDelOpen(true)
        } else {
            console.log('未设置 deleteAPI 或 item 数据异常')
        }
    }

    //删除数据
    function delData(values) {

        const api = `${deleteAPI.replace('(id)', delValue)}`;
        const queryData = { };
        promiseAjax(api, queryData, { method: 'DELETE' }).then(resp => {
            if (resp && resp.code === 200) {
                toastTips('删除成功')
                cb(true)
            } else {
                console.error("删除失败 == ", resp)
                toastTips('删除失败', 'error')
            }
        }).finally(_ => {
            setIsOpenEditModel(false)
        });
    }

    //处理额外提交的字段和值
    function handleFormData(data) {
        const newFormData = {
            ...formData,
            ...data
        }
        setFormData(newFormData)
    }

    //添加导航
    function addNavItem() {
        setModelTitle('添加类别')
        setIsOpenEditModel(true)
    }

    //删除导航
    function delNavItem() {
        setModelTitle('删除类别')
        setIsDelOpen(true)
    }

    //关闭模态框
    function onCloseEditTabModel() {
        reset()
        setIsOpenEditModel(false)
    }

    //根据type 加载表单组件
    function handleFormItem(list) {
        const fieldList = list;

        return fieldList.map((item, index) => {

            const { label, field, type, rules = { isRequired:false }  } = item;

            const C = formItemTypeMap[type]

            return <FormControl isRequired={rules.isRequired} isInvalid={rules.isRequired && rules.isRequired && errors[field]} key={`${index}_i`}>
                <FormLabel htmlFor={field}>{label}</FormLabel>
                <C {...item} register={register} errors={errors} defaultValue={currentData[field]} onChange={handleFormData} />
                <FormErrorMessage>
                    {errors[field] && errors[field].message}
                </FormErrorMessage>
            </FormControl>
        })
    }

    //验证数据
    function validateData(values) {

        return new Promise((resolve) => {
            setTimeout(() => {
                if (currentId) {
                    putData(values, currentId)
                } else {
                    postData(values)
                }
                resolve()
            }, 2000)
        })
    }

    function handleSwitchTab(item, index) {
        if (isSwtich) {
            getData(item.id)
            setCurrentId(item.id)
            setModelTitle('编辑导航类别')
            setIsOpenEditModel(true)
        } else {
            onSwitchTab(item, index)
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
        <>

            {items && items.length > 0 ? (
                <Tabs variant='enclosed' style={{ width: '900px' }} defaultIndex={tabIndex}>
                    <TabList>
                        {items.map((item, index) => {
                            if (item.id === '-1' && isSwtich) {
                                return <CustomAddTab key={`${index}_tab`} onClick={() => addNavItem()}></CustomAddTab>
                            }
                            if (item.id === '-2' && isSwtich) {
                                return <CustomDelTab key={`${index}_tab`} onClick={() => delNavItem()}></CustomDelTab>
                            }
                            return <Tab key={`${index}_tab`} onClick={() => handleSwitchTab(item, index)}>{item.name}</Tab>
                        })}
                    </TabList>
                    {/* <TabPanels>
                        {items.map((item, index) => (
                            <TabPanel key={`${index}_tabPanel`} >
                                {isLoading ? (
                                    <Spinner />
                                ) : (
                                    <Box>
                                        <AutoLayout {...config} onItemClick={onNavItemClick} cb={callback} isSwtich={isSwtich} />
                                    </Box>
                                )}
                            </TabPanel>
                        ))}

                    </TabPanels> */}
                </Tabs>
            ) : null}

            {/* 编辑模态框 */}
            <Modal
                initialFocusRef={initialRef}
                finalFocusRef={finalRef}
                isOpen={isOpenEditTabModel}
                onClose={onCloseEditTabModel}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>{modelTitle}</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        {isLoading ? (
                            <Spinner />
                        ) : (
                            <form onSubmit={handleSubmit(validateData)} noValidate>
                                <Stack spacing="2">
                                    {
                                        handleFormItem(tabFormConfig.fields)
                                    }
                                    <Stack direction='row' spacing={4} align='center'>
                                        <Button width='100px' colorScheme='teal' variant='solid' isLoading={isSubmitting} type='submit'>
                                            保存
                                        </Button>
                                        <Button width='100px' colorScheme='teal' variant='outline' onClick={onCloseEditTabModel}>取消</Button>
                                    </Stack>
                                </Stack>
                            </form>
                        )}

                    </ModalBody>
                </ModalContent>
            </Modal>


            {/* 删除提示模态框 */}
            <Modal isOpen={isDelOpen} onClose={() => setIsDelOpen(false)}>
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>{modelTitle}</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>

                        <form onSubmit={handleSubmit(delData)}>
                            <Stack spacing="5">
                                <FormControl>
                                    <FormLabel>类别</FormLabel>
                                    <RadioGroup onChange={setDelValue} value={delValue}>
                                        <Stack direction='row'>
                                            {items && items.map((item, index) => {
                                                if(item.id != '-1' && item.id != '-2'){
                                                   return <Radio value={item.id} key={`${index}_radio`}>{item.name}</Radio>
                                                }
                                            })}
                                        </Stack>
                                    </RadioGroup>
                                </FormControl>
                                <Stack direction='row' spacing={4} align='center'>
                                    <Button width='100px' colorScheme='teal' variant='solid' isLoading={isSubmitting} type='submit'>
                                        保存
                                    </Button>
                                    <Button width='100px' colorScheme='teal' variant='outline' onClick={()=>setIsDelOpen(false)}>取消</Button>
                                </Stack>
                            </Stack>
                        </form>
                    </ModalBody>
                </ModalContent>
            </Modal>

        </>
    )

}