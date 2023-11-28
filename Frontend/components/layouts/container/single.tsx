import * as React from 'react'
import Head from 'next/head'
import { ILayout } from '../types' ;
import Header from '../partial/header';

const SingleLayout = ({ children, pageTitle, showMeta }: ILayout) => {
    return (
        <div>
            <Head>
                <title>{pageTitle}</title>
            </Head>
            <Header />
            {children}
        </div>
    )
}

export default SingleLayout