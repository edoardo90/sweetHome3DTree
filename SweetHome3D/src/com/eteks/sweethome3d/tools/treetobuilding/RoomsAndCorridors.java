package com.eteks.sweethome3d.tools.treetobuilding;

import java.util.List;

import com.eteks.sweethome3d.model.Room;

public class RoomsAndCorridors {
    
      private List<Corridor> corridors;
      private List<Room>  rooms;
      
      public RoomsAndCorridors(List<Corridor> corridors, List<Room> rooms)
      {
        this.corridors = corridors;
        this.rooms = rooms;
      }
      
      public List<Room> getRooms() {
        return rooms;
      }
      public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
      }
      public List<Corridor> getCorridors() {
        return corridors;
      }
      public void setCorridors(List<Corridor> corridors) {
        this.corridors = corridors;
      }
}
