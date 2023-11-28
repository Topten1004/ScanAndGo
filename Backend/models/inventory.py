from start import db
from models.category import CategoryModel
from models.item import ItemModel
from models.building import BuildingModel
from models.detailLocation import DetailLocationModel
from sqlalchemy.orm import relationship
import datetime

class InventoryModel(db.Model):
    __tablename__ = 'inventories'
        
    id = db.Column(db.Integer, primary_key = True)
    category_id = db.Column(db.Integer, db.ForeignKey("categories.id"))
    item_id = db.Column(db.Integer, db.ForeignKey("items.id"))
    building_id = db.Column(db.Integer)
    area_id = db.Column(db.Integer)
    floor_id = db.Column(db.Integer)
    detail_location_id = db.Column(db.Integer)
    purchase_date = db.Column(db.String(120))
    last_date = db.Column(db.String(120))
    ref_client = db.Column(db.String(120))
    quantity = db.Column(db.Integer);
    quantity_go = db.Column(db.Integer);
    status = db.Column(db.Integer);
    photo = db.Column(db.TEXT)
    reg_date = db.Column(db.String(120), default = datetime.datetime.now().date())
    purchase_amount = db.Column(db.Integer);
    comment = db.Column(db.String(120));

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
                'sublocation_name': x.sublocation_name,
                'location_name': x.location_name,
                'purchase_date': x.purchase_date,
                'last_date': x.last_date,
                'ref_client': x.ref_client,
                'reg_date': x.reg_date
            }
        return list(
            map(lambda x: to_json(x), 
                db.session.query(
                    InventoryModel.id,
                    ItemModel.name.label('item_name'),
                    CategoryModel.name.label('category_name'),
                    DetailLocationModel.name.label('sublocation_name'),
                    BuildingModel.name.label('location_name'),
                    InventoryModel.purchase_date,
                    InventoryModel.last_date,
                    InventoryModel.ref_client,
                    InventoryModel.reg_date
                )
                .join(ItemModel, ItemModel.id == InventoryModel.item_id)
                .join(DetailLocationModel, DetailLocationModel.id == InventoryModel.sublocation_id)
                .join(BuildingModel, BuildingModel.id == DetailLocationModel.location_id)
                .order_by(InventoryModel.id).all()
            )
        )
        
    @classmethod
    def return_all_by_sublocation(cls, id):
        def to_json(x):
            return {
                'id': x.id,
                'item_name': x.item_name,
                'reg_date': x.reg_date,
                'barcode': x.barcode
            }
        return list(
            map(lambda x: to_json(x),
                db.session.query(
                    InventoryModel.id,
                    ItemModel.name.label('item_name'),
                    InventoryModel.reg_date,
                    ItemModel.barcode
                )
                .join(ItemModel, ItemModel.id == InventoryModel.item_id)
                .filter(InventoryModel.sublocation_id == id)
                .order_by(InventoryModel.id).all()
            )
        )
        