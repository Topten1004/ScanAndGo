import * as React from 'react'
import Head from 'next/head'
import { ILayout } from '../types' ;
import SideBar from '../partial/sidebar';

const BaseLayout = ({ children, pageTitle, showMeta }: ILayout) => {
    return (
        <div>
            <Head>
                <title>{pageTitle}</title>
            </Head>
            <div className='flex'>
                <SideBar />
                <div className='flex-1 bg-gray-100'>
                    {children}
                </div>
            </div>
        </div>
    )
}

export default BaseLayout