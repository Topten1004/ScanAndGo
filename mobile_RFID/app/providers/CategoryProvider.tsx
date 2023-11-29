import { createContext, useContext, useMemo, useState, useEffect, useCallback } from "react"
import createCategory from "../lib/category/createCategory"
import { ICategory } from "../types/category"
import deleteCategory from "../lib/category/deleteCategory"
import updateCategory from "../lib/category/updateCategory"
import getCategories from "../lib/category/getCategories"

const CategoryContext: any = createContext(null)

const CategoryProvider = ({ children }: any) => {
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(false)
  const [categoryName, setCategoryName] = useState("")
  const [isEditMode, setIsEditMode] = useState(false)
  const [editCategoryId, setEditCategoryId] = useState<number | null>(null)

  const addNewCategory = async () => {
    const response = await createCategory(categoryName)
    setCategoryName("")
    if (response?.error) return
    await getCategoryList()
  }

  const updateCategoryById = async () => {
    if (!editCategoryId) return
    const response = await updateCategory({
      id: editCategoryId,
      name: categoryName,
    })

    setIsEditMode(false)
    setCategoryName("")

    if (response?.error) return
    await getCategoryList()
  }

  const deleteCategoryById = async (id: number | null) => {
    const response = await deleteCategory(id)
    if (response?.error) return
    await getCategoryList()
  }

  const openEditMode = (data: ICategory) => {
    setIsEditMode(true)
    setCategoryName(data.name)
    setEditCategoryId(data.id)
  }

  const closeEditMode = () => {
    setIsEditMode(false)
    setCategoryName("")
    setEditCategoryId(null)
  }

  const getCategoryList = useCallback(async () => {
    setLoading(true)
    const response = await getCategories()
    setCategories(response)
    setLoading(false)
  }, [])

  useEffect(() => {
    getCategoryList()
  }, [getCategoryList])

  const value = useMemo(
    () => ({
        categories,
        getCategoryList,
        categoryName,
        setCategoryName,
        addNewCategory,
        loading,
        isEditMode,
        setIsEditMode,
        openEditMode,
        updateCategoryById,
        deleteCategoryById,
        closeEditMode,
    }),
    [
        categories,
        getCategoryList,
        categoryName,
        setCategoryName,
        addNewCategory,
        loading,
        isEditMode,
        setIsEditMode,
        openEditMode,
        updateCategoryById,
        deleteCategoryById,
        closeEditMode,
    ],
  )

  return <CategoryContext.Provider value={value}>{children}</CategoryContext.Provider>
}

export const useCategory = () => {
  const context: any = useContext(CategoryContext)
  if (!context) {
    throw new Error("useCategory must be used within a CategoryProvider")
  }
  return context
}

export default CategoryProvider
