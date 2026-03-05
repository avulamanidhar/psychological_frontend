export default function BottomNav() {
  return (
    <div className="h-[84px] bg-white border-t border-stroke-gray/60 flex items-center justify-around px-4">
      <div className="flex flex-col items-center gap-1 text-desc-gray text-[10px]">
        <div className="w-7 h-7 rounded-full border border-stroke-gray/60 flex items-center justify-center">
          <span className="text-sm">⌂</span>
        </div>
        <span>Home</span>
      </div>
      <div className="flex flex-col items-center gap-1 text-desc-gray text-[10px]">
        <div className="w-7 h-7 rounded-full border border-stroke-gray/60 flex items-center justify-center">
          <span className="text-sm">☺</span>
        </div>
        <span>Mood</span>
      </div>
      <div className="flex flex-col items-center gap-1 text-desc-gray text-[10px]">
        <div className="w-7 h-7 rounded-full border border-stroke-gray/60 flex items-center justify-center">
          <span className="text-sm">💬</span>
        </div>
        <span>Chat</span>
      </div>
      <div className="flex flex-col items-center gap-1 text-button-blue text-[10px]">
        <div className="w-10 h-10 rounded-2xl bg-button-blue/10 flex items-center justify-center">
          <span className="text-base">✨</span>
        </div>
        <span className="text-button-blue">Tools</span>
      </div>
      <div className="flex flex-col items-center gap-1 text-desc-gray text-[10px]">
        <div className="w-7 h-7 rounded-full border border-stroke-gray/60 flex items-center justify-center">
          <span className="text-sm">👤</span>
        </div>
        <span>Profile</span>
      </div>
    </div>
  )
}

