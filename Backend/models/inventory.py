from start import db
from models.category import CategoryModel
from models.subcategory import SubCategoryModel
from models.item import ItemModel
from models.sublocation import SubLocationModel
from models.location import LocationModel
from sqlalchemy.orm import relationship
import datetime

class InventoryModel(db.Model):
    __tablename__ = 'inventories'
        
    id = db.Column(db.Integer, primary_key = True)
    category_id = db.Column(db.Integer, db.ForeignKey("categories.id"))
    subcategory_id = db.Column(db.Integer, db.ForeignKey("subcategories.id"))
    item_id = db.Column(db.Integer, db.ForeignKey("items.id"))
    location_id = db.Column(db.Integer)
    sublocation_id = db.Column(db.Integer)
    purchase_date = db.Column(db.String(120))
    last_date = db.Column(db.String(120))
    ref_client = db.Column(db.String(120))
    reg_date = db.Column(db.String(120), default = datetime.datetime.now().date())
    
    item = relationship('ItemModel', back_populates='inventory_item_entries')
    subcategory = relationship('SubCategoryModel', back_populates='inventory_subcategory_entries')
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
                'subcategory_name': x.subcategory_name,
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
                    SubCategoryModel.name.label('subcategory_name'),
                    CategoryModel.name.label('category_name'),
                    SubLocationModel.name.label('sublocation_name'),
                    LocationModel.name.label('location_name'),
                    InventoryModel.purchase_date,
                    InventoryModel.last_date,
                    InventoryModel.ref_client,
                    InventoryModel.reg_date
                )
                .join(ItemModel, ItemModel.id == InventoryModel.item_id)
                .join(SubCategoryModel, SubCategoryModel.id == ItemModel.subcategory_id)
                .join(CategoryModel, CategoryModel.id == SubCategoryModel.category_id)
                .join(SubLocationModel, SubLocationModel.id == InventoryModel.sublocation_id)
                .join(LocationModel, LocationModel.id == SubLocationModel.location_id)
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
        