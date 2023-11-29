import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react"
import getCategories from "../lib/category/getCategories"
import { ICategory } from "../types/category"
import { IItem } from "../types/item"
import getItems from "../lib/item/getItems"

const LCIContext: any = createContext(null)

const LCIProvider = ({ children }: any) => {
  const [categories, setCategories] = useState([])
  const [selectedCategory, setSelectedCategory] = useState<ICategory | null>(null)
  const [items, setItems] = useState([])
  const [selectedItem, setSelectedItem] = useState<IItem | null>(null)
  const [itemsByCategory, setItemsByCategory] = useState([])

  const getCategoryList = useCallback(async () => {
    const response = await getCategories()
    if (response?.error) return
    setCategories(response)
  }, [])

  const getItemsList = useCallback(
    async () => {
      if (!selectedCategory) return
      const response = await getItems(selectedCategory.id)
      setItems(response)
    },
    [selectedCategory],
  )

  useEffect(() => {
    getCategoryList()
  }, [getCategoryList])

  useEffect(() => {
    getItemsList()
  }, [getItemsList])

  const value = useMemo(
    () => ({
      categories,
      setCategories,
      selectedCategory,
      setSelectedCategory,
      items,
      setItems,
      selectedItem,
      setSelectedItem,
      getItemsList,
      itemsByCategory,
    }),
    [
      categories,
      setCategories,
      selectedCategory,
      setSelectedCategory,
      items,
      setItems,
      selectedItem,
      setSelectedItem,
      getItemsList,
      itemsByCategory,
    ],
  )

  return <LCIContext.Provider value={value}>{children}</LCIContext.Provider>
}

export const useLCI = () => {
  const context: any = useContext(LCIContext)
  if (!context) {
    throw new Error("useLCI must be used within a LCIProvider")
  }
  return context
}

export default LCIProvider
