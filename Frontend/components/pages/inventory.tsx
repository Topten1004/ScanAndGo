import useInventoryForm from 'components/inventory/useInventoryForm';
import React, { useContext, useEffect, useRef, useState } from 'react'
import { useQuery } from 'react-query'
import { toast } from 'react-toastify';
import http from 'services/http-common'
import { LanguageContext } from 'shared/context/language';
import { excelSerialToJSDate } from 'shared/helper/common';
import { read, utils } from 'xlsx';

const InventoryPage = () => {

  const { dictionary } = useContext(LanguageContext);
  const classes = {
    label: '',
    select: 'px-2 py-1 w-40 border border-gray-200 rounded-md focus:outline-none',
    button: 'text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:outline-none font-medium rounded-lg text-sm px-5 py-2.5 text-center'
  }
  const fileInputRef = useRef(null);
  const [data, setData] = useState<any>([]);
  const { create, createIsLoading } = useInventoryForm();

  const getInventoryList: any = useQuery(
    ['getInventoryList', data],
    () => {
      return http.get(`/inventory/readall`)
    }
  )

  const inventoryList = React.useMemo(() => {
    return getInventoryList.data ?
      getInventoryList.data.data : []
  }, [getInventoryList.data])

  const handleImport = ($event: any) => {
    const files = $event.target.files;
    if (files.length) {
      const file = files[0];
      const reader = new FileReader();
      reader.onload = (event: any) => {
        const wb = read(event.target.result);
        const sheets = wb.SheetNames;

        if (sheets.length) {
          const rows: any = utils.sheet_to_json(wb.Sheets[sheets[0]]);

          // check valid csv file
          const row: any = Object.entries(rows)[0];

          if (!(Object.entries(row[1]).length >= 7 && Object.entries(row[1]).length <= 10)) {
            toast.warn("The specified format is not compatible");
            setData([]);
          } else {
            setData(rows.map((row: any) => {
              return {
                category: row?.LEVEL1,
                subcategory: row?.LEVEL2,
                item: row['REF ARTICLE'],
                refClient: row['REF CLIENT'],
                location: row?.LIEU,
                sublocation: row?.LIEU2,
                purchaseDate: typeof row['DATE ACHAT'] == 'number' ? excelSerialToJSDate(row['DATE ACHAT']) : row['DATE ACHAT'],
                lastDate: typeof row['DATE DERNIER INVENTAIRE'] == 'number' ? excelSerialToJSDate(row['DATE DERNIER INVENTAIRE']) : row['DATE DERNIER INVENTAIRE'],
                qty: row?.QTY
              }
            }))
          }
        }
      }
      reader.readAsArrayBuffer(file);
    }
  }

  const handleSave = () => {
    data.map((item: any) => {
      [...Array(item.qty)].map(_ => {
        create(item);
      })
    })
  }

  useEffect(() => {
    if (!createIsLoading)
      setData([]);
  }, [createIsLoading])

  return (
    <div className='px-12 py-16 space-y-12'>
      <div className='flex items-center gap-4'>
        <div>
          <input
            id='importExcel'
            type="file"
            ref={fileInputRef}
            accept=".xlsx, .xls, .csv"
            onChange={handleImport}
            hidden
          />
          <label htmlFor='importExcel'>
            <div className='flex items-center gap-12'>
              <button className={classes.button} onClick={() => {
                const ref: any = fileInputRef.current;
                ref.click();
              }}>{dictionary['import']}</button>
            </div>
          </label>
        </div>
        {data && data?.length > 0 && <button className={classes.button} onClick={handleSave}>{dictionary['save']}</button>}
      </div>

      <table className="w-full text-xs text-left text-gray-500 dark:text-gray-400">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" className="px-6 py-3">
              {dictionary["level1"]}
            </th>
            <th scope="col" className="px-6 py-3">
              {dictionary["level2"]}
            </th>
            <th scope="col" className="px-6 py-3">
              {dictionary["ref_article"]}
            </th>
            <th scope="col" className="px-6 py-3">
              {dictionary["date_achat"]}
            </th>
            <th scope="col" className="px-6 py-3">
              {dictionary["note"]}
            </th>
            <th scope="col" className="px-6 py-3">
              {dictionary["lieu"]}
            </th>
            <th scope="col" className="px-6 py-3">
              {dictionary['lieu2']}
            </th>
            <th scope="col" className="px-6 py-3">
              {dictionary['ref_client']}
            </th>
          </tr>
        </thead>
        <tbody>
          {
            inventoryList.status != -1 &&
            inventoryList?.map((item: any, index: number) => (
              <tr key={index} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                <th className="px-6 py-4">
                  {item.category_name}
                </th>
                <td className="px-6 py-4">
                  {item.subcategory_name}
                </td>
                <td className="px-6 py-4">
                  {item.item_name}
                </td>
                <td className="px-6 py-4">
                  {item.purchase_date}
                </td>
                <td className="px-6 py-4">
                  { }
                </td>
                <td className="px-6 py-4">
                  {item.location_name}
                </td>
                <td className="px-6 py-4">
                  {item.sublocation_name}
                </td>
                <td className="px-6 py-4">
                  {item.ref_client}
                </td>
              </tr>
            ))
          }
        </tbody>
      </table>
    </div>
  )
}

export default InventoryPage