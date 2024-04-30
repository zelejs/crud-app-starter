import React, { useState, useEffect, useRef }  from 'react';
import {
    Box,
    Modal,
    Button,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalFooter,
    ModalBody,
    ModalCloseButton,
    useDisclosure,
    useToast
} from '@chakra-ui/react';
import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout';
import promiseAjax from 'zero-element-boot/lib/components/utils/request';

export default function EditProps(props) {

    const { onActionCompleted, moduleName='' } = props;

    const toast = useToast()
    const initialRef = useRef()
    const finalRef = useRef()
    const { isOpen, onOpen, onClose } = useDisclosure()
    const [ onRefresh, setOnRefresh ] = useState(false)
    const [ currentProp, setCurrentProp ] = useState('')
    // const moduleName = LS.get('commonData').layoutName || ''

    const selectInitialRef = useRef()
    const selectFinalRef = useRef()
    const { isOpen:isSelectOpen, onOpen: onSelectOpen, onClose: onSelectClose } = useDisclosure()

    useEffect(() => {
        if (onRefresh) {
            setOnRefresh(false)
        }
    },[onRefresh])

    const onBtnClick = () => {
        onOpen()
    }

    const handleModalClose = () => {
        onClose()
        if (onActionCompleted) {
            onActionCompleted()
        }
    }

    const itemClick = (item) => {
        onSelectOpen()
        setCurrentProp(item)
    }

    const selectItemClick = (item) => {
        eidtData(item)
    }

    function eidtData(data) {

        const api = `/openapi/lc/module-props/${currentProp.id}`
        
        const queryData = {
            propValue:data.valueContent
        }
        
        promiseAjax(api, queryData, { method: 'PUT' }).then(resp => {
            if (resp && resp.code === 200) {
                toastTips('修改成功')
                setOnRefresh(true)
                onSelectClose(false)
            } else {
                console.error("修改失败 === ", resp)
                toastTips('修改失败', 'error')
            }
        }).finally(() => {
            setCurrentProp('')
        });
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

    console.log('currentProp = ', currentProp)

    return (
        <>
            <Button colorScheme='blue' onClick={onBtnClick}>
                编辑属性
            </Button>

            <Modal
                initialFocusRef={initialRef}
                finalFocusRef={finalRef}
                isOpen={isOpen}
                onClose={onClose}
                size='xl'
            >
                <ModalOverlay />
                <ModalContent >
                    <ModalHeader>{moduleName} 组件属性</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6} height={'500px'}>
                        { 
                            !onRefresh ? (
                                <PreviewAutoLayout layoutName='PropertyManage' moduleName={moduleName} onItemClick={itemClick} />
                            ) : <></> 
                        }
                    </ModalBody>
                    <ModalFooter justifyContent={'center'}>
                        <Button mr={3} onClick={onClose}>取消</Button>
                        <Button colorScheme='blue' onClick={handleModalClose}>确定</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            <Modal
                initialFocusRef={selectInitialRef}
                finalFocusRef={selectFinalRef}
                isOpen={isSelectOpen}
                onClose={onSelectClose}
                size='2xl'
            >
                <ModalOverlay />
                <ModalContent height={'500px'}>
                    <ModalHeader>属性列表</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody overflowX={'hiddin'} overflowY={'scroll'}>
                        <PreviewAutoLayout layoutName='PropsListAutolayout' searchPropName={currentProp.keyName} onItemClick={selectItemClick} />
                    </ModalBody>
                </ModalContent>
            </Modal>
        </>
    )
}