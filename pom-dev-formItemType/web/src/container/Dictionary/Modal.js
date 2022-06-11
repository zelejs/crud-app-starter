import React from 'react';
import ZEle from 'zero-element';
import { Modal } from 'antd';

export default function ModalWrapped({ visible, namespace, onClose, extraData }) {
  return <Modal
    title={config.title}
    visible={visible}
    onCancel={onClose}
    footer={null}
    bodyStyle={{
      padding: 0,
    }}
  >
    <ZEle
      namespace={namespace}
      config={config}
      extraData={extraData}
      onClose={onClose}
    />
  </Modal>
}

const config = {
  title: '新增字典值',
  layout: 'Empty',
  items: [
    {
      layout: 'Empty',
      component: 'Form',
      config: {
        API: {
          createAPI: '/api/config/field/options/<id>',
        },
        fields: [
          {
            field: 'name', label: '名称', type: 'input',
            rules: ['required']
          },
          { field: 'value', label: '值', type: 'input' },
        ]
      }
    }
  ]
};