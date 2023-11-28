from start import db
from sqlalchemy.orm import relationship

class ItemModel(db.Model):
    __tablename__ = 'items'
        
    id = db.Column(db.Integer, primary_key = True)
    category_id = db.Column(db.Integer, db.ForeignKey('categories.id'))
    name = db.Column(db.String(120), nullable = False)
    barcode = db.Column(db.String(120))

    inventory_item_entries = relationship('InventoryModel', back_populates='item')
    
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        
    @classmethod
    def find_by_id_name(cls, id, name):
        return ItemModel.query.filter_by(category_id = id, name = name).first()
    
    @classmethod
    def find_by_id(cls, id):
        return ItemModel.query.filter_by(id = id).first()
        
    @classmethod
    def return_all_by_id(cls, id):
        def to_json(x):
            return {
                'id': x.id,
                'category_id': x.category_id,
                'name': x.name,
                'isUsed': bool(x.inventory_item_entries)
            }
        return list(map(
            lambda x: to_json(x), 
            ItemModel.query
            .filter(ItemModel.category_id == id)
            .order_by(ItemModel.id)
            .all()
        ))

    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'category_id': x.category_id,
                'name': x.name,
                'barcode': x.barcode
            }
        return list(map(
            lambda x: to_json(x), 
            ItemModel.query
            .order_by(ItemModel.id)
            .all()
        ))


    @classmethod
    def delete_one(cls, id):
        try:
            row_deleted = cls.query.filter_by(id=id).first()
            db.session.delete(row_deleted)
            db.session.commit()

            return {'message': 'success'}
        except:
            return {'message': 'error'}
    
    @classmethod
    def update_one(cls, id, name, category_id, barcode):
        try:
            record = cls.query.get(id)
            record.name = name
            record.category_id = category_id
            record.barcode = barcode
            db.session.commit()

            return {'message': 'success'}
        except:
            return {'message': 'error'}
        
    @classmethod
    def assign_barcode(cls, id, barcode):
        try:
            record = cls.query.get(id)
            record.barcode = barcode
            db.session.commit()
            return {'message': 'success'}
        except:
            return {'message': 'error'}