package com.smartitventures.employeeapp;

/**
 * Created by dharamveer on 6/11/17.
 */

public class SpinnerKeyValue {

    public Integer id;
    public String name;

    public SpinnerKeyValue(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof SpinnerKeyValue){
            SpinnerKeyValue spinnerKeyValue = (SpinnerKeyValue) obj;

            if(spinnerKeyValue.getName().equals(name) && spinnerKeyValue.getId()==id)
                return true;
        }

        return false;

    }
}
