import BaseLayout from './container/base'
import SingleLayout from './container/single'
import { ILayout } from './types'

const layoutContainers = {
    base: BaseLayout,
    single: SingleLayout
}

interface ILayoutFactory extends ILayout {
    type: keyof typeof layoutContainers
}

const Layout = ({
    children,
    pageTitle,
    type,
    showMeta
}: ILayoutFactory) => {
    const Container = layoutContainers[type]

    return (
        <Container pageTitle={pageTitle} showMeta={showMeta}>
            {children}
        </Container>
    )
}

export default Layout