import React from 'react'

export default function helloworld (props) {
  const { massage = 'helloworld' } = props;
  return (
    <div>我是{massage} </div>
  )

}
