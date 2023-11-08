// ---------- Category ---------
// Create - Category
{
    'status': integer
}

// Read - Category
[
    {
        'id': integer,
        'name': string,
        'isUsed': boolean
    }
]

// Update, Delete - Category
null


// -------- SubCategory --------
// Create - SubCategory
{
    'message': string
}

// Read - SubCategory
[
    {
        'id': integer,
        'categoryId': integer,
        'name': string,
        'isUsed': boolean
    }
]

// Update, Delete - SubCategory
null


// --------- Item ----------
// Create - Item
{
    'message': string
}

// Read - Item
[
    {
        'id': integer,
        'subcategoryId': integer,
        'name': string,
        'isUsed': boolean
    }
]

// Update, Delete - Item
null


// ---------- Location -----------
// Create - Location
{
    'message': string
}

// Read - Location
[
    {
        'id': integer,
        'name': string
    }
]

// Update, Delete - Location
null


// ----------- SubLocation ---------
// Create - SubLocation
{
    'message': string
}

// Read - SubLocation
[
    {
        'id': integer,
        'locationId': integer,
        'name': string
    }
]

// Update, Delete - SubLocation
null