import {ReactNode} from 'react'

export interface ILayout {
    children: ReactNode
    pageTitle: string
    showMeta?: boolean
}