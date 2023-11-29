import showToast from "../shared/ui/showToast"

const handleError = (error: any) => {
  const primaryError = error?.reason || error?.data?.message
  const nestedError = error?.error?.message
  const fallbackError = error.message

  const toastMessage = primaryError || nestedError || fallbackError

  showToast(toastMessage)
}

export default handleError
