import SwiftUI
import ComposeApp

struct ContentView: View {
    let logic = Greeting()
    
    @State private var showContent = false
    @State private var info: DeviceInfo?
    
    var body: some View {
        ZStack {
            Color.black.edgesIgnoringSafeArea(.all)
            
            VStack(spacing: 20) {
                Text("APPLE SYSTEM MONITOR")
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundColor(.blue)
                
                Button(action: {
                    withAnimation {
                        self.info = logic.getSystemData()
                        self.showContent.toggle()
                    }
                }) {
                    Text(showContent ? "Abort Scan" : "Initialize Scan")
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
                
                if showContent, let data = info {
                    VStack(alignment: .leading, spacing: 15) {
                        InfoRowView(label: "Sistema:", value: data.osName)
                        InfoRowView(label: "Versión:", value: data.osVersion)
                        InfoRowView(label: "Dispositivo:", value: data.deviceModel)
                        InfoRowView(label: "Resolución:", value: data.screenResolution)
                    }
                    .padding()
                    .background(Color(UIColor.darkGray))
                    .cornerRadius(15)
                    .transition(.scale)
                }
            }
            .padding()
        }
    }
}

struct InfoRowView: View {
    var label: String
    var value: String
    
    var body: some View {
        HStack {
            Text(label).foregroundColor(.gray)
            Spacer()
            Text(value).foregroundColor(.white).fontWeight(.medium)
        }
    }
}



