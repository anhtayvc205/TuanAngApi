JSONObject json = new JSONObject();
json.put("name", data.name);
json.put("break", data.breakBlock);
json.put("place", data.placeBlock);
json.put("kill", data.kill);
json.put("death", data.death);
json.put("playtime", data.formatPlaytime());
json.put("lastSeen", data.lastSeenText());
