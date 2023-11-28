from routes.user import userRouteIndex
from routes.category import categoryRouteIndex
from routes.item import itemRouteIndex
from routes.role import roleRouteIndex
from routes.building import buildingRouteIndex
from routes.detaillocation import detailLocationRouteIndex
from routes.inventory import inventoryRouteIndex
from routes.floor import floorRouteIndex
from routes.area import areaRouteIndex

def Route_index(app):
    userRouteIndex(app)
    categoryRouteIndex(app)
    itemRouteIndex(app)
    roleRouteIndex(app)
    buildingRouteIndex(app)
    detailLocationRouteIndex(app)
    inventoryRouteIndex(app)
    floorRouteIndex(app)
    areaRouteIndex(app)