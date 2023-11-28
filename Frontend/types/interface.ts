export interface INavItem {
    label: string
    link: string
}

export interface ISession {
    sessionId: string
    userId: number
    dtCreated: string
    dtLastUsed: string
    ipAddress: string
}

export interface IUser {
    id: number
    username: string
    password: string
    role: number
}

export interface IRole {
    id: number
    name: string
}