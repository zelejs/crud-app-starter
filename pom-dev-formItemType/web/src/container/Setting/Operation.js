import React from 'react';
import { Button, Switch } from 'antd';

export default function Operation({
  value, data, index,
  editID,
  onEdit, onEditCancel, onSubmit,
}) {
  const { valueType } = data;
  const isThisEdit = data.id === editID;
  const disabled = editID > -1 && !isThisEdit;

  function handleInputSave(e) {
    onSubmit(
      (data.field || data.id),
      e.target.value
    );
  }
  function handleSwitchSave(checked) {
    let value;
    if (checked) {
      value = 'true';
    } else {
      value = 'false';
    }
    onSubmit((data.field || data.id),
      value
    );
  }

  return <div>
    {valueType === 'boolean' ? (
      <Switch
        checked={data.value === 'true'}
        onChange={handleSwitchSave}
      />
    ) : (
        isThisEdit ? (
          <>
            <Button size="small" onClick={onEditCancel}>取消</Button>
            <Button size="small"
              type="primary"
              className="ZEleA-margin-left"
              onClick={handleInputSave}
            >
              保存
          </Button>
          </>
        ) : (
            <Button size="small" type="primary"
              disabled={disabled}
              onClick={onEdit.bind(null, data.id, data.value)}
            >
              编辑
          </Button>
          )
      )}
  </div>
}