from start import db
from models.category import CategoryModel
from models.item import ItemModel
from models.building import BuildingModel
from models.area import AreaModel
from models.floor import FloorModel
from models.detaillocation import DetailLocationModel

from sqlalchemy.orm import relationship
import datetime

class InventoryModel(db.Model):
    __tablename__ = 'inventories'
        
    id = db.Column(db.Integer, primary_key = True)
    category_id = db.Column(db.Integer, db.ForeignKey("categories.id"))
    item_id = db.Column(db.Integer, db.ForeignKey("items.id"))
    building_id = db.Column(db.Integer, db.ForeignKey("buildings.id"))
    area_id = db.Column(db.Integer, db.ForeignKey("areas.id"))
    floor_id = db.Column(db.Integer, db.ForeignKey("floors.id"))
    detail_location_id = db.Column(db.Integer, db.ForeignKey("detail_locations.id"))
    purchase_date = db.Column(db.String(120))
    last_date = db.Column(db.String(120))
    ref_client = db.Column(db.String(120))
    status = db.Column(db.Integer)
    photo = db.Column(db.String)
    reg_date = db.Column(db.String(120), default = datetime.datetime.now().date())
    comment = db.Column(db.String(120))
    rfid = db.Column(db.String(120))
    barcode = db.Column(db.String(120))

    item = relationship('ItemModel', back_populates='inventory_item_entries')
    category = relationship('CategoryModel', back_populates='inventory_category_entries')
    
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'item_name': x.item_name,
                'category_name': x.category_name,
                'building_name': x.building_name,
                'area_name': x.area_name,
                'floor_name': x.floor_name,
                'detail_location_name': x.detail_location_name,
                'purchase_date': x.purchase_date,
                'last_date': x.last_date,
                'ref_client': x.ref_client,
                'reg_date': x.reg_date
            }
        
        query_result = db.session.query(
            InventoryModel.id,
            ItemModel.name.label('item_name'),
            CategoryModel.name.label('category_name'),
            BuildingModel.name.label('building_name'),
            AreaModel.name.label('area_name'),
            FloorModel.name.label('floor_name'),
            DetailLocationModel.name.label('detail_location_name'),
            InventoryModel.purchase_date,
            InventoryModel.last_date,
            InventoryModel.ref_client,
            InventoryModel.reg_date
        ).join(ItemModel, ItemModel.id == InventoryModel.item_id) \
            .join(CategoryModel, CategoryModel.id == InventoryModel.category_id) \
            .join(BuildingModel, BuildingModel.id == InventoryModel.building_id) \
            .join(AreaModel, AreaModel.id == InventoryModel.area_id) \
            .join(FloorModel, FloorModel.id == InventoryModel.floor_id) \
            .join(DetailLocationModel, DetailLocationModel.id == InventoryModel.detail_location_id) \
            .order_by(InventoryModel.id).all()

        return list(map(lambda x: to_json(x), query_result))
    
    @classmethod
    def return_all_by_detaillocation(cls, id):
        def to_json(x):
            return {
                'id': x.id,
                'item_name': x.item_name,
                'reg_date': x.reg_date,
                'category_id': x.category_id,
                'barcode': x.barcode
            }
        return list(
            map(lambda x: to_json(x),
                db.session.query(
                    InventoryModel.id,
                    ItemModel.name.label('item_name'),
                    InventoryModel.reg_date,
                    CategoryModel.id.label('category_id'),
                    ItemModel.barcode,
                )
                .join(ItemModel, ItemModel.id == InventoryModel.item_id)
                .join(CategoryModel, CategoryModel.id == InventoryModel.category_id)
                .filter(InventoryModel.detail_location_id == id)
                .order_by(InventoryModel.id).all()
            )
        )
        
    @classmethod
    def updateInventory(cls, id, barcode, status, photo):
        try:
            record = cls.query.get(id)
            # Ensure the record is found before accessing its attributes
            if record:

                record.status = status
                record.photo = photo
                db.session.commit()

                return ItemModel.assign_barcode(record.item.id, barcode)
            else:
                return {'message': 'Record not found'}
        except Exception as e:
            print(e)
            return {'message': 'Error updating inventory'}
