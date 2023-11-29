import { createContext, useContext, useMemo, useState, useEffect, useCallback } from "react"
import createItem from "../lib/item/createItem"
import { IItem } from "../types/item"
import deleteItem from "../lib/item/deleteItem"
import updateItem from "../lib/item/updateItem"
import { useNavigation } from "@react-navigation/native"
import getItems from "../lib/item/getItems"

const ItemContext: any = createContext(null)

const ItemProvider = ({ children }: any) => {
  const navigation = useNavigation()

  const states: any = navigation.getState()

  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(false)
  const [itemName, setItemName] = useState("")
  const [isEditMode, setIsEditMode] = useState(false)
  const [editItemId, setEditItemId] = useState<number | null>(null)

  const categoryId = useMemo(() => {
    return states.routes.find((route: any) => route.name === "Item").params.categoryId
  }, [states])

  const addNewItem = async () => {
    const response = await createItem(itemName)
    setItemName("")
    if (response?.error) return
    getItemList()
  }

  const updateItemById = async () => {
    if (!editItemId) return
    const response = await updateItem({
      id: editItemId,
      name: itemName,
      category_id: categoryId,
      barcode: ""
    })

    setIsEditMode(false)
    setItemName("")

    if (response?.error) return
    getItemList()
  }

  const deleteItemById = async (id: number | null) => {
    const response = await deleteItem(id)
    if (response?.error) return
    getItemList()
  }

  const openEditMode = (data: IItem) => {
    setIsEditMode(true)
    setItemName(data.name)
    setEditItemId(data.id)
  }

  const closeEditMode = () => {
    setIsEditMode(false)
    setItemName("")
    setEditItemId(null)
  }

  const getItemList = useCallback(async () => {
    if (!categoryId) return
    setLoading(true)
    const response = await getItems(categoryId)
    if (response?.error) return
    setItems(response)
    setLoading(false)
  }, [categoryId])

  useEffect(() => {
    getItemList()
  }, [getItemList])

  const value = useMemo(
    () => ({
      items,
      getItemList,
      itemName,
      setItemName,
      addNewItem,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateItemById,
      deleteItemById,
      closeEditMode,
      categoryId
    }),
    [
      items,
      getItemList,
      itemName,
      setItemName,
      addNewItem,
      loading,
      isEditMode,
      setIsEditMode,
      openEditMode,
      updateItemById,
      deleteItemById,
      closeEditMode,
      categoryId
    ],
  )

  return <ItemContext.Provider value={value}>{children}</ItemContext.Provider>
}

export const useItem = () => {
  const context: any = useContext(ItemContext)
  if (!context) {
    throw new Error("useItem must be used within a ItemProvider")
  }
  return context
}

export default ItemProvider
