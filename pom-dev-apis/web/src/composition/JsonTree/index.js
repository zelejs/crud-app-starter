import React from 'react';
import TreeItem from './TreeItem';
import TreeArrayItem from './TreeArrayItem';
import TreeObjectItem from './TreeObjectItem';

/**
 * 
 * @param {data } data  json
 * 
 */

export default function Tree(props) {

    const keys = Object.keys(props)
    
    // console.log('keys ==', keys)
    return (
        <>
            {
                keys && keys.length > 0 ?
                    (keys && keys.map((key, i) => (
                        <div key={i}>
                            {(isValueObject(props, key)) ?
                                <>
                                    {/* <Tree {...getValue(props, key)} /> */}
                                    <TreeObjectItem keyName={key}{...props} />
                                </>
                                :
                                (isValueArray(props, key)) ?
                                    <TreeArrayItem keyName={key}{...props} />
                                    :
                                    <TreeItem keyName={key} {...props} />
                            }
                        </div>
                    ))
                    ) : <></>
            }
        </>
    )
}


// 获取Value值
function getValue(props, key) {
    return props[key]
}

// 判断value是否为Object
function isValueObject(props, key) {
    const obj = props[key]
    return (obj && typeof (obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]")
}

// 判断value是否为Array
function isValueArray(props, key) {
    const obj = props[key]
    return ((obj && Array.isArray(obj)))
}