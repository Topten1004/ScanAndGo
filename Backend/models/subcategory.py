from start import db
from sqlalchemy.orm import relationship

class SubCategoryModel(db.Model):
    __tablename__ = 'subcategories'
        
    id = db.Column(db.Integer, primary_key = True)
    category_id = db.Column(db.Integer, db.ForeignKey("categories.id") , nullable = False)
    name = db.Column(db.String(120), nullable = False) # should be fixed
    
    inventory_subcategory_entries = relationship('InventoryModel', back_populates='subcategory')
    
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        
    @classmethod
    def find_by_id_name(cls, id, name):
        return SubCategoryModel.query.filter_by(category_id = id, name = name).first()
    
    @classmethod
    def find_by_id(cls, id):
        return SubCategoryModel.query.filter_by(id = id).first()
        
    @classmethod
    def return_all_by_id(cls, id):
        def to_json(x):
            return {
                'id': x.id,
                'categoryId': x.category_id,
                'name': x.name,
                'isUsed': bool(x.inventory_subcategory_entries)
            }
        return list(map(
            lambda x: to_json(x), 
            SubCategoryModel.query
            .filter(SubCategoryModel.category_id == id)
            .order_by(SubCategoryModel.id)
            .all()
        ))

    @classmethod
    def delete_one(cls, id):
        try:
            row_deleted = cls.query.filter_by(id=id).first()
            db.session.delete(row_deleted)
            db.session.commit()
        except:
            return {'message': 'error'}
    
    @classmethod
    def update_one(cls, id, name):
        try:
            record = cls.query.get(id)
            record.name = name
            db.session.commit()
        except:
            return {'message': 'error'}