from routes.user import userRouteIndex
from routes.category import categoryRouteIndex
from routes.subcategory import subcategoryRouteIndex
from routes.item import itemRouteIndex
from routes.role import roleRouteIndex
from routes.location import locationRouteIndex
from routes.sublocation import sublocationRouteIndex
from routes.inventory import inventoryRouteIndex

def Route_index(app):
    userRouteIndex(app)
    categoryRouteIndex(app)
    subcategoryRouteIndex(app)
    itemRouteIndex(app)
    roleRouteIndex(app)
    locationRouteIndex(app)
    sublocationRouteIndex(app)
    inventoryRouteIndex(app)