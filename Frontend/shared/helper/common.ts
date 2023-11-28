import { format } from "date-fns";

export function excelSerialToJSDate(serial: number) {
    // Calculate the number of milliseconds for a day
    const MS_PER_DAY = 24 * 60 * 60 * 1000;

    // Calculate the base date for Excel serial dates (December 30, 1899)
    const baseDate = new Date('1899-12-31T00:00:00.000Z');

    // Calculate the date by adding the serial number of days
    const resultDate = format(new Date(baseDate.getTime() + serial * MS_PER_DAY), 'MM/dd/yyyy').substring(0, 10);

    return resultDate;
}