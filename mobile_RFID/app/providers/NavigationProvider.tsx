import { createContext, useContext, useMemo } from "react"

const NavigationContext: any = createContext(null)

const NavigationProvider = ({ children }: any) => {
  const value = useMemo(() => ({}), [])

  return <NavigationContext.Provider value={value}>{children}</NavigationContext.Provider>
}

export const useNavigation = () => {
  const context = useContext(NavigationContext)
  if (!context) {
    throw new Error("useNavigation must be used within a NavigationProvider")
  }
  return context
}

export default NavigationProvider
