import React, { useState } from 'react';
import styles from './index.less'

const itemStyleMap = {
    get: { methodBg: '#61AFFE', itemBg: '#EBF3FB', itemBorder: '1px solid #61AFFE', hover: '#007fff'},
    post: { methodBg: '#49CC90', itemBg: '#E8F6F0', itemBorder: '1px solid #49CC90', hover: '#00b763'},
    put: { methodBg: '#FCA130', itemBg: '#FBF1E6', itemBorder: '1px solid #FCA130', hover: '#dd7a00'},
    delete: { methodBg: '#F93E3E', itemBg: '#FAE7E7', itemBorder: '1px solid #F93E3E', hover: '#d10000'},
}

/**
 * 
 */
export default function (props) {

    const { api, name, apiMethod, index=0 } = props;
    
    const methodTxt = apiMethod.toLowerCase()

    const itemStyle = itemStyleMap[methodTxt]
    
    const [onHover, setOnHover] = useState(false);

    const toggleHover = () => {
        const result = !onHover;
        setOnHover(result)
    }

    let hoverColor = `${itemStyle.methodBg}`;
    if (onHover) {
        hoverColor = `${itemStyle.hover}`;
    } else {
        hoverColor = `${itemStyle.methodBg}`;
    }

    return (
        <div className={styles.swagger_item} style={{ backgroundColor: itemStyle.itemBg, border: itemStyle.itemBorder, borderColor: hoverColor }}
            onMouseEnter={() => toggleHover()} onMouseLeave={() => toggleHover()}
        >
            <div className={styles.api_method} style={{backgroundColor: itemStyle.methodBg}}>
                {`${apiMethod || ''}`}
            </div>

            <div className={styles.textColor}>
                {`${api || ''}`}
            </div>

            <div style={{width:'15px', height:'1px'}}></div>

            {
                name && (
                    <div className={styles.note}>
                        {`${name}`}
                    </div>
                )
            }
        </div>
            
    )
    
 

}